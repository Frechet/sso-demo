import { Component, Inject, OnInit, Optional } from '@angular/core';
import { User } from '../dto/user';
import { ActivatedRoute } from '@angular/router';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { KeycloakService } from '../keycloak-service/keycloak.service';
import { SERVICE_URL } from '../app.component';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  user: User;

  private _userId: string;
  private serviceUrl: string = 'http://localhost:8090/backend';

  constructor(
    private _route: ActivatedRoute,
    private _http: Http,
    private _kc: KeycloakService,
    @Optional()@Inject(SERVICE_URL) serviceUrl: string
  ) {
    if(serviceUrl) {
      this.serviceUrl = serviceUrl;
    }
    this._userId = this._route.snapshot.paramMap.get('user-id');
  }

  ngOnInit() {
    if (this._userId) {
      this.getUser();
    }
  }

  authenticated(): boolean {
    return this._kc.authenticated();
  }

  getUser() {
    const url = this.serviceUrl + '/user/';
    const headers = new Headers({'Accept': 'application/json'});
    const options = new RequestOptions({ headers: headers, withCredentials: true, params: {
      userId: this._userId
    }});
    console.log(url);
    this._http.get(url, options)
      .map(res => res.json())
      .subscribe(data => {
        this.user = data;
      }, (error: Response) => this.handleServiceError(error));
  }

  private handleServiceError(error: Response) {
    console.log(error);
  }

}
