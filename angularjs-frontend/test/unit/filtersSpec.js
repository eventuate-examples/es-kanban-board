/**
 * Created by ANDREW on 9/3/2015.
 */
import 'angular-mocks';
import app from '../../app/scripts/App';

describe('filter', () => {

  //beforeEach(angular.mock.module('kanbanApp'));

  // http://stackoverflow.com/questions/25558765/angularjs-karma-filter-unknown-provider
  let filter;

  beforeEach(angular.mock.module('kanbanApp'));
  beforeEach(angular.mock.inject((_$filter_) => {
    filter = _$filter_;
  }));

  describe('taskStatus', () => {

    it('should ..', () =>{

      let source = [{ status: 'backlog'}];
      expect(filter('taskStatus')(source, 'completed').length).toBe(0);
      expect(filter('taskStatus')(source, 'backlog').length).toBe(1);

    });

  });

  //describe('toJSON', () => {
  //
  //
  //  it('should ..', () =>{
  //
  //    let source = [{ status: 'backlog'}];
  //    console.log(filter);
  //    console.log(filter('toJSON1'));
  //
  //    //expect(filter('toJSON')(source)).toBe(JSON.stringify(source));
  //
  //  });
  //
  //});
});