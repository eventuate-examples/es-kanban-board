/**
 * Created by andrew on 29/12/15.
 */
'use strict';

import 'babel-polyfill';

console.log('kanban.spec.js is loaded');

import { getRandomEmail }  from './helpers/random';

xdescribe('Logging in to the app', () => {

  const email = getRandomEmail();
  console.log(`Login email generated: ${email}`);

  beforeAll(done => {
    browser.get('/');
    browser.sleep(10000).then(() => {
      const loginUrl = browser.getCurrentUrl();

      loginUrl.then((urlVal) => {
        if (!/\/login$/i.test(urlVal)) {
          console.log(urlVal);
          if (browser.isElementPresent(element(by.linkText("Sign Out")))) {
            element(by.linkText("Sign Out")).click();
          }
        }
        done();

      }, done);

    });

  });

  it('first visit lands on a login page', (done) => {
    browser.get('/');
    browser.sleep(1000).then(() => {
      const loginUrl = browser.getCurrentUrl();
      expect(loginUrl).toMatch(/\/login$/i);
      done();
    });
  });

  it('allows logging-in for any email', done => {
    element(by.id("inputEmail1")).sendKeys(email);
    element(by.css("button.btn.btn-default")).click();

    browser.sleep(10000).then(() => {

      const loggedInUrl = browser.getCurrentUrl();
      //console.log(loggedInUrl);
      expect(loggedInUrl).not.toMatch(/\/login$/i);
      expect(loggedInUrl).toMatch(/\/$/);
      done();
    });

  });

  it('when logged-in, log out option is available. Leads to login page', done => {

    const signOutLnk = element(by.linkText("Sign Out"));

    expect(signOutLnk.isPresent()).toBe(true);

    signOutLnk.click();

    browser.sleep(1000).then(() => {
      const loginUrl = browser.getCurrentUrl();
      expect(loginUrl).toMatch(/\/login$/i);
      done();
    });

  });

  describe('When logged in..', () => {

    beforeAll(done => {
      browser.get('/');
      browser.sleep(10000).then(() => {
        const loginUrl = browser.getCurrentUrl();

        loginUrl.then((urlVal) => {
          if (!/\/login$/i.test(urlVal)) {
            console.log(urlVal);
            if (browser.isElementPresent(element(by.linkText("Sign Out")))) {
              element(by.linkText("Sign Out")).click();
            }
          }
          element(by.id("inputEmail1")).sendKeys(email);
          element(by.css("button.btn.btn-default")).click();

          browser.sleep(1000).then(() => {
            const loggedInUrl = browser.getCurrentUrl();
            loggedInUrl.then(urlVal => {
              console.log(urlVal);
            });

            done();
          });

        }, done);

      });

    });

    xit('has all the steps recorded earlier', (done) => {
      const actions = [
        () => {
          return element(by.xpath("//div[@class='b_board_list']/div[1]/div/div[2]/div")).click();
        },
        () => {
          return element(by.linkText("Close ×")).click();
        },
        () => {
          return element(by.xpath("//div[@class='b_board_list']//span[.='2']")).click();
        },
        () => {      return element(by.linkText("Close ×")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board_list']/div[1]/div/div[4]/div")).click();
        },
        () => {      return element(by.linkText("Close ×")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board_list']/div[1]/div/div[1]/div")).click();
        },
        () => {      return element(by.css("button.btn.btn-primary")).click();
        },
        () => {      return element(by.id("inpBoardName")).sendKeys("New test board");
        },
        () => {      return element(by.css("div.modal-body")).click();
        },
        () => {      return element(by.id("inpBoardDesc")).sendKeys("simple description");
        },
        () => {      return element(by.css("button.btn.btn-primary")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board_list']//h4[.='New test board']")).click();
        },
        () => {      return element(by.css("div.b_cnt.m_hor")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board']//div[normalize-space(.)='']")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board']/div[3]/div[2]/div/div/div")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board']/div[3]/div[3]/div/div/div")).click();
        },
        () => {      return element(by.linkText("Create Task")).click();
        },
        () => {      return element(by.id("inpTaskTitle")).sendKeys("test task 01");
        },
        () => {      return element(by.id("inpTaskDesc")).sendKeys("simple description");
        },
        () => {      return element(by.css("div.modal-footer")).click();
        },
        () => {      return element(by.css("button.btn.btn-primary")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_cnt-body']/div/div[3]/div[2]")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board']/div[3]/div[1]/div/div/div")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board']//div[normalize-space(.)='']")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board']/div[3]/div[2]/div/div/div")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board']/div[3]/div[3]/div/div/div")).click();
        },
        () => {      return element(by.linkText("H")).click();
        },
        () => {      return element(by.css("div.modal-dialog > div.modal-content > div.modal-footer > button.btn.btn-default")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board']/div[3]/div[3]/div/div/div")).click();
        },
        () => {      return element(by.css("div.b_cnt.m_hor")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_cnt-body']/div/div[3]/div[1]/a[2]")).click();
        },
        () => {      return element(by.xpath("//div[@id='deleteTaskModal']/div/form/div/div[2]/div[1]/input")).click();
        },
        () => {      return element(by.xpath("//div[@id='deleteTaskModal']/div/form/div/div[2]/div[2]/input")).click();
        },
        () => {      return element(by.xpath("//div[@id='deleteTaskModal']/div/form/div/div[2]/div[3]/textarea")).click();
        },
        () => {      return element(by.css("button.btn.btn-danger")).click();
        },
        () => {      return element(by.linkText("Close ×")).click();
        },
        () => {      return element(by.xpath("//div[@class='b_board_list']/div[1]/div/div[14]/div")).click();
        },
        () => {
          done();
        }
      ];

      let count = 1;
      actions.reduce((p, next) => {
        return p.then(() => {
          try {
            console.log(count++);
            const result = next();
            if (result.then) {
              result.then(() => {
                return true;
              }, (err) => {
                console.log(err);
                return true;
              });
            }
          } catch (ex) {
            console.log(ex);
          }
          return browser.sleep(150);
        });
      }, Promise.resolve(true));


    });

    it('allows to add a board', done => {

      browser.get('/');
      browser.sleep(500).then(() => {


        const addBoard = element.all(by.css('[data-target="#newBoardModal"]'));
        expect(addBoard.count()).toEqual(1);
        expect(element(by.css('#newBoardModal')).isDisplayed()).toBe(false);


        addBoard.click();
        browser.sleep(500).then(() => {

          expect(element(by.css('#newBoardModal')).isDisplayed()).toBe(true);
          done();
        });

      });



    });


  });

});
