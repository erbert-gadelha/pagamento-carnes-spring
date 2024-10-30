
const root = document.querySelector(':root');
const scroll = {
  last: 0,
  delta: 0,
  height: 0,
  clamp: 0
};

const navbarHeight = getComputedStyle(document.body).getPropertyValue('--navbar-height');
scroll.height = Number(navbarHeight.replace(/\D/g, ''));
scroll.clamp = 8 - scroll.height ;


const clamp = function(min, value, max) {
  return Math.min(Math.max(value, min), max);
};

document.addEventListener("scroll", (event) => {
  const diff = scroll.last-window.scrollY;
  if(((diff > 0) && scroll.delta == 0) || ((diff < 0) && scroll.delta == scroll.clamp)) {
      scroll.last = window.scrollY;
      return;
  }

  scroll.delta = clamp (scroll.clamp, scroll.delta + diff, 0);
  scroll.last = window.scrollY;

  root.style.setProperty('--navbar-delta', `${scroll.delta}px`);
  if(scroll.last-window.scrollY < 0 || scroll.delta == scroll.clamp) {
      root.style.setProperty('--navbar-opacity', 1);
  } else {
    const ratio = scroll.delta/scroll.height;
    root.style.setProperty('--navbar-opacity', 1+(2*ratio/3));
  }
  //console.log(scroll)
});
