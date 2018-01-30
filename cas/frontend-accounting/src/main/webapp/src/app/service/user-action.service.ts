import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import { USER_DETAILS_PATH } from '../utils/constants';

@Injectable()
export class UserActionService {

  constructor(
    private _router: Router,
    private _location: Location
  ) {}

  navigateToBack() {
    this._location.back();
  }

  navigateToUserDetails(userId: number): void {
    if (userId) {
      this._router.navigate([ USER_DETAILS_PATH.replace(':user-id', userId.toString()) ]);
    }
  }
}
