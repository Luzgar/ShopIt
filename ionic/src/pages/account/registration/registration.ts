import { Authentification } from './../authentification/authentification';
import {Component} from "@angular/core";
import {NavController} from "ionic-angular";
import {UserService} from "../../../service/UserService";
import {FormBuilder, FormGroup, Validators, AbstractControl} from "@angular/forms";
import {LoginValidator} from "../../../validators/LoginValidator";
import {PseudoValidator} from "../../../validators/PseudoValidator";
import {HomePage} from "../../home/home";



@Component({
  selector: 'registration',
  templateUrl: 'registration.html',
  providers: [UserService],
})

export class Registration {

  submitAttempt: boolean;
  slideOneForm: FormGroup;
  login: AbstractControl;
  pseudo: AbstractControl;
  password: AbstractControl;

  constructor(public navCtrl: NavController, public formBuilder: FormBuilder, public userService: UserService, public loginValidator: LoginValidator, public pseudoValidator: PseudoValidator) {
    this.slideOneForm = formBuilder.group({

      login: ['', Validators.compose([Validators.maxLength(10), Validators.minLength(4), Validators.pattern('[a-zA-Z0-9]*'), Validators.required]), this.loginValidator.checkLogin.bind(this.loginValidator)],
      pseudo: ['', Validators.compose([Validators.maxLength(10), Validators.minLength(4), Validators.pattern('[a-zA-Z0-9]*'), Validators.required]), this.pseudoValidator.checkPseudo.bind(this.pseudoValidator)],
      password: ['', Validators.compose([Validators.minLength(4), Validators.pattern('[a-zA-Z0-9]*'), Validators.required])]
    });
  }

  save(){
    console.log(this.slideOneForm);
    this.submitAttempt = true;

    if(!this.slideOneForm.valid){
      console.log("ERREUR");
    }
    else {
      console.log(this.slideOneForm.value);
      this.userService.createAccount(this.slideOneForm.value.login, this.slideOneForm.value.pseudo, this.slideOneForm.value.password);
      this.navCtrl.setRoot(HomePage);
      this.navCtrl.push(Authentification);
    }

  }

}
