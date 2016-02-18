/**
 * Created by ANDREW on 9/1/2015.
 */
//import * as ng from 'angular';
import 'angular-mocks';
import '../../app/scripts/components/index';
import { assert, expect, should} from 'chai';

//http://www.yearofmoo.com/2013/01/full-spectrum-testing-with-angularjs-and-karma.html#testing-filters

describe('Unit: Testing Controllers', ()=> {

  beforeEach(angular.mock.module('kanbanApp'));

  it('should have a KanbanBoardController controller', function() {

    expect(angular.module('kanbanApp').KanbanBoardController).not.to.equal(null);
  });


  describe('KanbanBoardController', ()=> {

    beforeEach(angular.mock.module('kanbanApp'));
    //inject(function($rootScope, $controller, $httpBackend) {
    //  var searchTestAtr = 'cars';
    it('should create "tasks" model with 3 tasks', angular.mock.inject(($controller) => {

      let scope = {},
        ctrl = $controller('KanbanBoardController', {
          $scope: scope
        });

      expect(scope.tasks.length).to.equal(3);

    }));
  });
});