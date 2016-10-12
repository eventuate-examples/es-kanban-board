/**
 * Created by andrew on 31/12/15.
 */
describe('Selenium Test Case', function() {

  it('should execute test case without errors', done => {
    //var text, value, bool, source, url, title;
    //var TestVars = {};
    browser.get("/");
    browser.sleep(10000).then(() => {
      done();
    });
  });

});