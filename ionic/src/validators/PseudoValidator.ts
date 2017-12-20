import { FormControl } from '@angular/forms';
import {UserService} from "../service/UserService";
import {Injectable} from "@angular/core";

@Injectable()
export class PseudoValidator {

  constructor(public userService: UserService){}

  checkPseudo(control: FormControl): any {
    return new Promise(resolve => {
      this.userService.isPseudoExisting(control.value).subscribe((data) => {
        console.log("RESULTAT : "+ data);
        if(data == true){
          resolve({
            "pseudo taken": true
          });

        } else {
          resolve(null);
        }
      });
    });
  }
}
