import { Component, OnInit } from '@angular/core';
import { User } from '../dto/user';
import { ActivatedRoute } from '@angular/router';
import { JwksValidationHandler, OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from '../config/auth-config';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html'
})
export class LoginPageComponent implements OnInit {

  user: User;

  constructor(
    private _route: ActivatedRoute,
    private _oauthService: OAuthService
  ) {
    this.user = {
      email: '',
      password: ''
    }
  }

  ngOnInit() {
  }

  login() {
    this._oauthService.initImplicitFlow();
  }

  logout() {
    this._oauthService.logOut();
  }

  private handleServiceError(error: Response) {
    console.log(error);
  }

}
