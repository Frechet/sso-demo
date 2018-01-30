import { Component, InjectionToken } from '@angular/core';
import { authConfig } from './config/auth-config';
import { JwksValidationHandler, OAuthService } from 'angular-oauth2-oidc';

export const SERVICE_URL = new InjectionToken<string>('serviceUrl');

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {


  constructor(private _oauthService: OAuthService) {
    this.configureOAuth();
  }

  private configureOAuth() {
    this._oauthService.configure(authConfig);
    // TODO: add validation
    // this._oauthService.tokenValidationHandler = new JwksValidationHandler();
    this._oauthService.loadDiscoveryDocumentAndTryLogin();
  }
}
