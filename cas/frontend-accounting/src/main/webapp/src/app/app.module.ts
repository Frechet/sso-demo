import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent, SERVICE_URL } from './app.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { AppRoutingModule } from './app-routing.module';
import { LoginPageComponent } from './login-page/login-page.component';
import { environment } from '../environments/environment';
import { FormsModule } from '@angular/forms';
import { UserActionService } from './service/user-action.service';
import { OAuthModule, OAuthService } from 'angular-oauth2-oidc';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    UserDetailsComponent,
    LoginPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: ['http://localhost:8090/backend'],
        sendAccessToken: true
      }
    }),
    AppRoutingModule
  ],
  providers: [
    UserActionService,
    OAuthService,
    { provide: SERVICE_URL, useValue: environment.serviceUrl }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
