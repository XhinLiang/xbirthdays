import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {RegisterService} from '../../services/api/register.service';
import {NgForm} from '@angular/forms';

@Component({
    selector: 's-login-pg',
    templateUrl: './register.component.html',
    styleUrls: ['./register.scss'],
})

export class RegisterComponent implements OnInit {
    @ViewChild('f') form: NgForm;
    model: any = {
        email: '',
        nickname: '',
        password: '',
        repeatedPassword: '',
    };
    registerMessage = '';

    constructor(private router: Router,
                private registerService: RegisterService) {
    }

    ngOnInit() {
        this.form.control.valueChanges.subscribe(value => {
            if (value.password !== value.repeatedPassword) {
                this.registerMessage = 'password doesn\'t match!';
            } else {
                this.registerMessage = '';
            }
        });
    }

    register() {
        this.registerService.doRegister(this.model.email, this.model.nickname, this.model.password)
            .subscribe(() => {
                    this.registerMessage = 'register success!!';
                    this.goLogin()
                },
                err => {
                    this.registerMessage = err.message;
                }
            );
    }

    goLogin() {
        this.router.navigate(['/login'])
    }
}
