import { AuthConfig } from 'angular-oauth2-oidc';
import { environment } from '../../environments/environment';

export const authConfig: AuthConfig = {

  oidc: true,

  // Url of the Identity Provider
  issuer: environment.ssoUrl,

  // URL of the SPA to redirect the user to after login
  redirectUri: window.location.origin + '/user-details',

  // why is it not true ?
  strictDiscoveryDocumentValidation: false,

  // The SPA's id. The SPA is registerd with this id at the auth-server
  clientId: 'frontend-accounting',

  // set the scope for the permissions the client should request
  // The first three are defined by OIDC. The 4th is a usecase-specific one
  scope: 'openid customscope',
};
