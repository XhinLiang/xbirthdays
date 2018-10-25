import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './home.component';

import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from "./pages/register/register.component";
import {LogoutComponent} from './pages/logout/logout.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';

import {AuthGuard} from './services/auth-guard.service';
import {PageNotFoundComponent} from './pages/404/page-not-found.component';
import {ProfileComponent} from "./pages/profile/profile.component";
import {RecordsComponent} from "./pages/records/records.component";

export const routes: Routes = [
    //Important: The sequence of path is important as the router go over then in sequential manner
    {path: '', redirectTo: '/home/dashboard/profile', pathMatch: 'full'},
    {
        path: 'home',
        component: HomeComponent,
        canActivate: [AuthGuard],
        children: [  // Children paths are appended to the parent path
            {
                path: '',
                redirectTo: '/home/dashboard',
                pathMatch: 'full',
                data: [{selectedHeaderItemIndex: 1, selectedSubNavItemIndex: -1}]
            },
            // Default path (if no deep path is specified for home component like
            // webui/home then it will by default show ProductsComponent )
            {
                path: 'dashboard',
                component: DashboardComponent,
                data: [{selectedHeaderItemIndex: 0, selectedSubNavItemIndex: -1}],
                children: [
                    {path: '', redirectTo: '/home/dashboard/profile', pathMatch: 'full'},
                    {
                        path: 'profile',
                        component: ProfileComponent,
                        data: [{selectedHeaderItemIndex: 0, selectedSubNavItemIndex: 0}]
                    },
                    {
                        path: 'stat',
                        component: ProfileComponent,
                        data: [{selectedHeaderItemIndex: 0, selectedSubNavItemIndex: 1}]
                    }
                ]
            },
            {
                path: 'records',
                component: RecordsComponent,
                data: [{selectedHeaderItemIndex: 1, selectedSubNavItemIndex: -1}]
            }
        ]
    },
    {path: 'login', component: LoginComponent, data: [{selectedHeaderItemIndex: -1, selectedSubNavItemIndex: -1}]},
    {
        path: 'register',
        component: RegisterComponent,
        data: [{selectedHeaderItemIndex: -1, selectedSubNavItemIndex: -1}]
    },
    {path: 'logout', component: LogoutComponent, data: [{selectedHeaderItemIndex: -1, selectedSubNavItemIndex: -1}]},
    {path: '**', component: PageNotFoundComponent, data: [{selectedHeaderItemIndex: -1, selectedSubNavItemIndex: -1}]}

];

@NgModule({
    imports: [RouterModule.forRoot(routes, {useHash: true})],
    exports: [RouterModule],
    declarations: [PageNotFoundComponent]
})
export class AppRoutingModule {
}
