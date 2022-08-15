import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from './error/error.component';
import { FetchdetailsComponent } from './fetchdetails/fetchdetails.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { WelcomeComponent } from './welcome/welcome.component';

const routes: Routes = [
  { path:'',component: LoginComponent},
  { path:'welcome',component: WelcomeComponent,},
  { path:'getPensionDetails',component: FetchdetailsComponent,},
  { path:'logout',component: LogoutComponent},
  { path:'**',component: ErrorComponent}
];

// add for securing routes canActivate:[RouteGuardService]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
