import { Component, Inject, OnInit, Optional } from '@angular/core';
import { User } from '../dto/user';
import { ActivatedRoute } from '@angular/router';
import { SERVICE_URL } from '../app.component';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html'
})
export class UserDetailsComponent implements OnInit {

  user: User;

  private _userId: string;
  private serviceUrl: string = 'http://localhost:8090/backend';

  constructor(
    private _route: ActivatedRoute,
    private _http: HttpClient,
    private _oauthService: OAuthService,
    @Optional()@Inject(SERVICE_URL) serviceUrl: string
  ) {
    if(serviceUrl) {
      this.serviceUrl = serviceUrl;
    }
    this._userId = this._route.snapshot.paramMap.get('user-id');
  }

  ngOnInit() {
    this._oauthService.tryLogin().then(
      () => this.update()
    );
  }

  update() {
    const claims = this._oauthService.getIdentityClaims();
    console.log(claims);

    this._userId = claims['id'];
    if (this._userId) {
      this.getUser();
    }
  }

  logout() {
    this._oauthService.logOut();
  }

  getUser() {
    const url = this.serviceUrl + '/user/';
    const headers = new HttpHeaders({'Accept': 'application/json'});
    // headers.append("Authorization", "Bearer " + this._oauthService.getAccessToken());
    let params = new HttpParams();
    params = params.append('userId', this._userId);
    this._http.get<User>(url, { headers: headers, observe: 'body', withCredentials: true, params: params })
      .subscribe(data => {
        this.user = data;
      }, (error: Response) => this.handleServiceError(error));
  }

  private handleServiceError(error: Response) {
    console.log(error);
  }

}
