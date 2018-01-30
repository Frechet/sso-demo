import { Component, Inject, InjectionToken, OnInit, Optional } from '@angular/core';

import { Http, Headers, RequestOptions, Response, URLSearchParams } from '@angular/http';
import { KeycloakService } from './keycloak-service/keycloak.service';
import { User } from './dto/user';

import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

export const SERVICE_URL = new InjectionToken<string>('serviceUrl');
export const SUPER_USER_LOGIN = new InjectionToken<string>('publicUserLogin');
export const SUPER_USER_PASSWORD = new InjectionToken<string>('publicUserPassword');

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  private serviceUrl: string = 'http://localhost:8090/backend';

  private publicUserLogin: string = 'guest';
  private publicUserPassword: string = 'guest';

  user: User;

  constructor(
    private http: Http,
    private kc: KeycloakService,
    @Optional()@Inject(SERVICE_URL) serviceUrl: string,
    @Optional()@Inject(SUPER_USER_LOGIN) superUserLogin: string,
    @Optional()@Inject(SUPER_USER_PASSWORD) superUserPassword: string)
  {
    if (serviceUrl) {
      this.serviceUrl = serviceUrl;
    }
    if (superUserLogin) {
      this.publicUserLogin = superUserLogin;
      this.publicUserPassword = superUserPassword;
    }
    this.user = {
      email: null,
      password: null
    }
  }

  ngOnInit(): void {
    this.loginBySuperUser();
  }

  loginBySuperUser(): void {
    if (!this.authenticated()) {
      const url = KeycloakService.keycloakAuth.authServerUrl +
        '/realms/' + KeycloakService.keycloakAuth.realm +
        '/protocol/openid-connect/token';
      const headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded'});
      let body = new URLSearchParams();
      body.set('client_id', KeycloakService.keycloakAuth.clientId);
      body.set('username', this.publicUserLogin);
      body.set('password', this.publicUserPassword);
      body.set('grant_type', 'password');
      const options = new RequestOptions({headers: headers});
      this.http.post(url, body, options)
        .map(res => res.json())
        .subscribe(data => {
          this.kc.setToken(data.access_token, data.refresh_token, data.id_token);
        }, (error: Response) => this.handleServiceError(error));
    }
  }

  saveNewUser() {
    console.log(this.user);
    if (this.authenticated()) {
      this.sendNewUser();
    } else {
      console.error("Registration service not authorized...")
    }
  }

  private sendNewUser() {
    const url = this.serviceUrl + '/user/save';
    this.http.put(url, this.user)
      .subscribe(() => {}, (error: Response) => this.handleServiceError(error));
  }

  authenticated(): boolean {
    return this.kc.authenticated();
  }

  // login() {
  //   this.loginBySuperUser();
  // }

  private handleServiceError(error: Response) {
    console.log(error);
  }
}
