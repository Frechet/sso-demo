import { Component, OnInit } from '@angular/core';
import { User } from '../dto/user';
import { KeycloakService } from '../keycloak-service/keycloak.service';
import { Headers, Http, RequestOptions, Response, URLSearchParams } from '@angular/http';
import { UserActionService } from '../service/user-action.service';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  user: User;

  constructor(
    private _route: ActivatedRoute,
    private _http: Http,
    private _kc: KeycloakService,
    private _userActionService: UserActionService
  ) {
    this.user = {
      email: '',
      password: ''
    }
  }

  ngOnInit() {
  }

  login() {
    const url = KeycloakService.keycloakAuth.authServerUrl +
      '/realms/' + KeycloakService.keycloakAuth.realm +
      '/protocol/openid-connect/token';
    const headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded'});
    let body = new URLSearchParams();
    body.set('client_id', KeycloakService.keycloakAuth.clientId);
    body.set('username', this.user.username);
    body.set('password', this.user.password);
    body.set('grant_type', 'password');
    const options = new RequestOptions({headers: headers});
    this._http.post(url, body, options)
      .map(res => res.json())
      .subscribe(data => {
        this._kc.setToken(data.access_token, data.refresh_token, data.id_token);
        this._userActionService.navigateToUserDetails(this._kc.getTokenParsed().user_id);
      }, (error: Response) => this.handleServiceError(error));
  }

  private handleServiceError(error: Response) {
    console.log(error);
  }

}
