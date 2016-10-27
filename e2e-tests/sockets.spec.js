/**
 * Created by andrew on 27/01/16.
 */
import 'babel-polyfill';
import chai from 'chai';
import chaiJsonSchema from 'chai-json-schema';
// import * as injector from './helpers/injector';
import * as schemas from './helpers/schemas';
import * as ws from './helpers/ws-helper';

import dragDrop from './helpers/draggability';

import { getRandomEmail, getRandomLatinAlpha }  from './helpers/random';

const EC = protractor.ExpectedConditions;

chai.use(chaiJsonSchema);

console.log('nodejs-server/test/e2e/sockets.spec.js is loaded');

const login = (testEmail) => {

  //# Go to the login URL
  browser.get('/#/login', 200);

  return browser.sleep(500).then(() => {

    //# Fill out the form and click the button
    element(by.id("inputEmail1")).sendKeys(testEmail);
    element(by.css("button.btn.btn-default")).click();

    return browser.sleep(500);

  });

};

describe('Subscribe to sockets', () => {

  afterEach(function() {
    browser.manage().logs().get('browser').then(function(browserLog) {
      // expect(browserLog.length).toEqual(0);
      // Uncomment to actually see the log.
      console.log('log: ' + require('util').inspect(browserLog));
    });
  });


  const boardTitle = `New test board ${ getRandomLatinAlpha(8) }`;
  const boardDescription = `New test board ${ getRandomLatinAlpha(8) }`;
  const testEmail = getRandomEmail();

  it('should bring me to boards list when logged in', (done) => {


    //# Overwrite the socketService module with a mocked version
    login(testEmail).then(() => {

      expect(browser.getLocationAbsUrl()).toBe('/');
      done();

    });

  });

  it('should be able to create a board and receive BoardCreatedEvent', (done) => {

    browser.get('/');
    console.log(new Date() - 0);

    browser.sleep(700).then(() => {
      console.log(new Date() - 0);

      console.log('Open dialog..');
      const addBoard = element(by.css('[data-target="#newBoardModal"]'));

      // console.log('Expecting #newBoardModal trigger to be present');
      // expect(addBoard.isDisplayed()).toBe(true);
      browser.wait(EC.elementToBeClickable(addBoard), 10000, "The link addBoard is still not clickable");

      addBoard.click();

      console.log(new Date() - 0);
      return browser.sleep(700);
    }).then(() => {
      console.log('Fill dialog..');
      console.log(new Date() - 0);

      console.log('Expecting #newBoardModal modal to be shown');

      const newModal = element(by.css('#newBoardModal'));
      browser.wait(EC.presenceOf(newModal), 10000, "The newModal is still not clickable");

      element(by.id("inpBoardName")).sendKeys(boardTitle);
      element(by.id("inpBoardDesc")).sendKeys(boardDescription);

      browser.sleep(1000).then(() => {
        element(by.css("button.btn.btn-primary")).click();
      });

      return ws.BoardCreatedEventHelper();

    }).then((...messages) => {
      console.log('processing messages..');
      console.log(messages);

      expect(messages.length).toEqual(1);
      const [{ type = null, data = {}, msg = null} = {}, ...rest] = messages;
      // console.log(rest);

      expect(type).toEqual("BoardCreatedEvent");

      expect(data.title).toEqual(boardTitle);
      expect(data.updatedBy).toEqual(testEmail);
      expect(data.createdBy).toEqual(testEmail);

      chai.expect(data).to.be.jsonSchema(schemas.BoardCreatedEvent);

      return browser.sleep(7000);
    }).then(() => {
      console.log('board created');

      console.log(new Date() - 0);
      console.log('select this board:', `//div[@class='b_board_title']//h4[.='${boardTitle}']`);

      element(by.xpath(`//div[contains(@class, "b_board_title")]//h4[contains(text(),'${boardTitle}') or contains('${boardTitle}', text())]`)).click();

      return browser.sleep(1000);
    }).then(done);

  });

  describe('Tasks events', () => {

    //beforeAll((done) => {
    //  element(by.xpath(`//div[@class='b_board_list']//h4[.='${boardTitle}']`)).click();
    //  browser.sleep(1000);
    //});
    //
    // beforeEach((done) => {
    //   injector.clearReceivedSocketMessages().then(done);
    // });
    //
    // afterEach((done) => {
    //   injector.clearReceivedSocketMessages().then(done);
    // });

    const taskTitle = `test task ${ getRandomLatinAlpha(8) }`;
    const taskDescription = `test task description ${ getRandomLatinAlpha(8) }`;

    let taskId = null;

    it('Tasks: 1. TaskCreatedEvent', (done) => {
      console.log(new Date() - 0);
      console.log('click on create task ');

      const createTaskLink =  element(by.linkText("Create Task"));

      browser.wait(EC.elementToBeClickable(createTaskLink), 10000, "The link createTaskLink is still not clickable");

      createTaskLink.click();

      browser.sleep(500).then(() => {
        console.log(new Date() - 0);

        element(by.id("inpTaskTitle")).sendKeys(taskTitle);
        element(by.id("inpTaskDesc")).sendKeys(taskDescription);

        browser.sleep(1000).then(() => {
          element(by.css("button.btn.btn-primary")).click();
        });

        return ws.TaskCreatedEventHelper();
        // return element.all(by.xpath(`//div[@data-task-id]`)).count().then(function(count) {
        //   console.log('Counted ' + count + ' tasks.');
        //   return browser.wait(EC.presenceOf(element(by.xpath(`(//div[@data-task-id])[${ count + 1 }]`))), 10000, "The task is not created");
        // });
       }).then((...messages) => {

        console.log(messages);

        expect(messages.length).toEqual(1);
        const [{ type = null, data = {}, msg = null} = {}, ...rest] = messages;
        console.log(rest);

        expect(type).toEqual("TaskCreatedEvent");

        expect(data.title).toEqual(taskTitle);
        expect(data.description).toEqual(taskDescription);
        expect(data.updatedBy).toEqual(testEmail);
        expect(data.createdBy).toEqual(testEmail);

        taskId = data.id;

        expect(taskId).not.toBeNull();

        chai.expect(data).to.be.jsonSchema(schemas.TaskCreatedEvent);

        return browser.sleep(500);

      }).then(done);

    });

    it('Tasks: 2. TaskScheduledEvent', (done) => {

      console.log('Drag task onto "scheduled"');
      console.log('Task ID:' + taskId);

      const taskToDrag = element(by.xpath(`//div[@data-task-id='${taskId}']`));
      const columnToDragTo = element(by.xpath(`//div[@data-column-status='scheduled']`));

      expect(taskToDrag.isPresent()).toBe(true);
      expect(columnToDragTo.isPresent()).toBe(true);

      // injector.clearReceivedSocketMessages();

      browser.sleep(1000).then(() => {
        dragDrop(taskToDrag, columnToDragTo);
      });

      ws.TaskScheduledEventHelper().then((...messages) => {

        console.log(messages);

        expect(messages.length).toEqual(1);
        const [{ type = null, data = {}, msg = null} = {}, ...rest] = messages;
        console.log(rest);

        expect(type).toEqual("TaskScheduledEvent");
        chai.expect(data).to.be.jsonSchema(schemas.TaskScheduledEvent);
        done();

      });

    });

    it('Tasks: 3. TaskStartedEvent', (done) => {

      console.log('Drag task onto "started"');

      const taskToDrag = element(by.xpath(`//div[@data-task-id='${taskId}']`));
      const columnToDragTo = element(by.xpath(`//div[@data-column-status='started']`));

      expect(taskToDrag.isPresent()).toBe(true);
      expect(columnToDragTo.isPresent()).toBe(true);

      browser.sleep(1000).then(() => {
        dragDrop(taskToDrag, columnToDragTo);
      });

      ws.TaskStartedEventHelper().then((...messages) => {

        console.log(messages);

        expect(messages.length).toEqual(1);
        const [{ type = null, data = {}, msg = null} = {}, ...rest] = messages;
        console.log(rest);

        expect(type).toEqual("TaskStartedEvent");
        chai.expect(data).to.be.jsonSchema(schemas.TaskStartedEvent);

        done();
      });

    });

    it('Tasks: 4. TaskCompletedEvent', (done) => {

      console.log('Drag task onto "completed"');

      const taskToDrag = element(by.xpath(`//div[@data-task-id='${taskId}']`));
      const columnToDragTo = element(by.xpath(`//div[@data-column-status='completed']`));

      expect(taskToDrag.isPresent()).toBe(true);
      expect(columnToDragTo.isPresent()).toBe(true);

      browser.sleep(1000).then(() => {
        dragDrop(taskToDrag, columnToDragTo);
      });

      ws.TaskCompletedEventHelper().then((...messages) => {

        console.log(messages);

        expect(messages.length).toEqual(1);
        const [{ type = null, data = {}, msg = null} = {}, ...rest] = messages;
        console.log(rest);

        expect(type).toEqual("TaskCompletedEvent");
        chai.expect(data).to.be.jsonSchema(schemas.TaskCompletedEvent);

        done();
      });

    });

    it('Tasks: 5. TaskBacklogEvent', (done) => {

      console.log('Drag task onto "backlog"');

      const taskToDrag = element(by.xpath(`//div[@data-task-id='${taskId}']`));
      const columnToDragTo = element(by.xpath(`//div[@data-column-status='backlog']`));

      expect(taskToDrag.isPresent()).toBe(true);
      expect(columnToDragTo.isPresent()).toBe(true);

      browser.sleep(1000).then(() => {
        dragDrop(taskToDrag, columnToDragTo);
      });

      ws.TaskBacklogEventHelper().then((...messages) => {

        console.log(messages);

        expect(messages.length).toEqual(1);
        const [{ type = null, data = {}, msg = null} = {}, ...rest] = messages;
        console.log(rest);

        expect(type).toEqual("TaskBacklogEvent");
        chai.expect(data).to.be.jsonSchema(schemas.TaskBacklogEvent);

        done();

      });

    });

    it('Tasks: 6. TaskDeletedEvent', (done) => {

      console.log('Deleting task');
      element(by.xpath(`//div[@data-task-id='${taskId}']//a[2]`)).click();

      browser.sleep(700).then(() => {
        element(by.css("button.btn.btn-danger")).click();
      });

      ws.TaskDeletedEventHelper().then((...messages) => {

        console.log(messages);

        expect(messages.length).toEqual(1);
        const [{ type = null, data = {}, msg = null} = {}, ...rest] = messages;
        console.log(rest);

        expect(type).toEqual("TaskDeletedEvent");

        console.log(data);

        chai.expect(data).to.be.jsonSchema(schemas.TaskDeletedEvent);

        done();
      });

    });

  });

});