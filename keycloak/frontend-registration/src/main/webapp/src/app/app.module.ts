import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent, SERVICE_URL, SUPER_USER_LOGIN, SUPER_USER_PASSWORD } from './app.component';
import { KeycloakService } from './keycloak-service/keycloak.service';
import { KEYCLOAK_HTTP_PROVIDER } from './keycloak-service/keycloak.http';
import { HttpModule } from '@angular/http';
import { environment } from '../environments/environment';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [
    KeycloakService,
    KEYCLOAK_HTTP_PROVIDER,
    { provide: SERVICE_URL, useValue: environment.serviceUrl },
    { provide: SUPER_USER_LOGIN, useValue: environment.publicUserLogin },
    { provide: SUPER_USER_PASSWORD, useValue: environment.publicUserPassword }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
