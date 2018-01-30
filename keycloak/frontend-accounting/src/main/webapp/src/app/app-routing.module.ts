import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { LOGIN_PATH, USER_DETAILS_PATH } from './utils/constants';

const routes: Routes = [
  {path: LOGIN_PATH, component: LoginPageComponent},
  {path: USER_DETAILS_PATH, component: UserDetailsComponent},
  {path: '**', redirectTo: LOGIN_PATH, pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
