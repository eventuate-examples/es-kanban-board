/**
 * Created by andrew on 28/01/16.
 */
export default function(elementToDrag, elementToDragTo) {

  const targetOffset = { x: -10, y: -10 };
  browser.actions().
    mouseMove(elementToDrag).
    mouseDown(elementToDrag).
    mouseMove(elementToDragTo, targetOffset).
    mouseUp(elementToDragTo, targetOffset).
    perform();

  return browser.sleep(300);

}