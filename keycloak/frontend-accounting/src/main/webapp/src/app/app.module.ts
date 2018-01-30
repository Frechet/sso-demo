import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent, SERVICE_URL } from './app.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { AppRoutingModule } from './app-routing.module';
import { LoginPageComponent } from './login-page/login-page.component';
import { KeycloakService } from './keycloak-service/keycloak.service';
import { KEYCLOAK_HTTP_PROVIDER } from './keycloak-service/keycloak.http';
import { environment } from '../environments/environment';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { UserActionService } from './service/user-action.service';

@NgModule({
  declarations: [
    AppComponent,
    UserDetailsComponent,
    LoginPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule
  ],
  providers: [
    UserActionService,
    KeycloakService,
    KEYCLOAK_HTTP_PROVIDER,
    { provide: SERVICE_URL, useValue: environment.serviceUrl }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
