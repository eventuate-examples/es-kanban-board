/**
 * Created by andrew on 29/01/16.
 */
function* getRandom(templateSet, len) {
  if (len) {
    yield templateSet[Math.floor(Math.random() * templateSet.length)];
    yield* getRandom(templateSet, len - 1);
  }
}

export function getRandomLatinAlpha(len) {
  const templateSet = 'qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM';
  return [...getRandom(templateSet, len)].join('');
}

export function getRandomEmail() {
  return (`${getRandomLatinAlpha(20)}@${getRandomLatinAlpha(10)}.${getRandomLatinAlpha(3)}`).toLowerCase();
}