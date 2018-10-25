import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
// Third Party Modules
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {ClarityModule} from '@clr/angular';
// Local App Modules
import {AppRoutingModule} from './app-routing.module';
// Directives
import {TrackScrollDirective} from './directives/track_scroll/track_scroll.directive';
// Components
import {BadgeComponent} from './components/badge/badge.component';
import {LegendComponent} from './components/legend/legend.component';
import {LogoComponent} from './components/logo/logo.component';
// Pages  -- Pages too are components, they contain other components
import {AppComponent} from './app.component';
import {HomeComponent} from './home.component';
import {LoginComponent} from './pages/login/login.component';
import {LogoutComponent} from './pages/logout/logout.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {RegisterComponent} from './pages/register/register.component';
// Services
import {AppConfig} from './app-config';
import {UserInfoService} from './services/user-info.service';
import {AuthGuard} from './services/auth-guard.service';
import {ApiRequestService} from './services/api/api-request.service';
import {LoginService} from './services/api/login.service';
import {RegisterService} from './services/api/register.service';
import {
    MatButtonModule,
    MatDatepickerModule,
    MatDialogModule,
    MatInputModule,
    MatNativeDateModule,
    MatOptionModule,
    MatSelectModule
} from "@angular/material";
import {AddRecordDialog} from "./components/dialog/add-record-dialog.component";
import {ProfileComponent} from "./pages/profile/profile.component";
import {RecordsComponent} from "./pages/records/records.component";
import {ProfileService} from "./services/api/profile.service";
import {RecordsService} from "./services/api/records.service";


@NgModule({

    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,

        // Thirdparty Module
        NgxDatatableModule,
        NgxChartsModule,
        ClarityModule.forChild(),

        // Local App Modules
        AppRoutingModule,
        MatDialogModule,
        MatButtonModule,
        MatInputModule,
        MatOptionModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule,

    ],

    declarations: [
        // Components
        BadgeComponent,
        LegendComponent,
        LogoComponent,
        AddRecordDialog,

        // Pages -- Pages too are components, they contain other components
        AppComponent,
        HomeComponent,
        LoginComponent,
        RegisterComponent,
        LogoutComponent,
        DashboardComponent,
        ProfileComponent,
        RecordsComponent,
        AddRecordDialog,

        // Directives
        TrackScrollDirective,
    ],

    entryComponents: [
        AddRecordDialog,
    ],

    providers: [
        AuthGuard,
        UserInfoService,
        ApiRequestService,
        LoginService,
        ProfileService,
        RecordsService,
        RegisterService,
        AppConfig,
    ],

    bootstrap: [AppComponent]
})

export class AppModule {
}
