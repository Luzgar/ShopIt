import { FormControl } from '@angular/forms';
import {UserService} from "../service/UserService";
import {Injectable} from "@angular/core";

@Injectable()
export class LoginValidator {

  constructor(public userService: UserService){}

  checkLogin(control: FormControl): any {
    return new Promise(resolve => {
      this.userService.isLoginExisting(control.value).subscribe((data) => {
        console.log("RESULTAT : "+ data);
        if(data == true){
          resolve({
            "username taken": true
          });

        } else {
          resolve(null);
        }
      });
    });
  }
}
