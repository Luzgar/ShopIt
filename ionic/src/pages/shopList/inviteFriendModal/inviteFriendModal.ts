import {Component} from "@angular/core";
import {NavController, NavParams} from "ionic-angular";
import {ListService} from "../../../service/ListService";
import {ColorService} from "../../../service/ColorService";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'invite-friend',
  templateUrl: 'inviteFriendModal.html',
  providers: [ListService, ColorService]
})

export class InviteFriend {

  colorPerPseudo: any;
  listId: number;
  pseudo: string;
  checked: true;
  ownerPseudo: number;
  collabInfos: any;
  userId: number;
  pic: string;
  color: any;

  constructor(public navCtrl: NavController, public navParams: NavParams, public listService: ListService, public colorService: ColorService, private _sanitizer: DomSanitizer) {
    this.colorPerPseudo = navParams.get("collabInfos");
    this.listId = navParams.get("listId");
    this.ownerPseudo = navParams.get("ownerPseudo");
    this.userId = navParams.get("userId");
    this.pic = "assets/icon/profil-circle.png";
    this.color = navParams.get("color");
    this.defineMedalImage();
  }

  ajouter(){
    this.listService.addCollaborateur(this.listId, this.pseudo);
    this.navCtrl.pop();
  }

  checkBox(){
    console.log(this.checked);
    this.checked = true;

  }

  deleteUser(collab){

    console.log("Foo Fighters");
    this.listService.removeCollaborateur(this.listId, collab.pseudo);
    this.colorPerPseudo.splice(this.colorPerPseudo.indexOf(collab), 1);
  }

  blockquoteColor(item){
    return this._sanitizer.bypassSecurityTrustStyle("5px solid "+this.colorService.getHexaByName(item.color));

  }

  defineMedalImage(){
    this.collabInfos = [];
    for(var i=0; i<this.colorPerPseudo.length;i++){
      this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/img/medals/"+this.colorPerPseudo[i].medal+".png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    }
    // for(var i=0; i<this.colorPerPseudo.length;i++){
    //   switch(this.colorPerPseudo[i].medal){
    //     case "Rodeur des rayons":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       console.log(this.colorPerPseudo[i].color);
    //       break;
    //     case "Leader":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Cuistot":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Photographe":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Boucher":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Vegan":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Pecheur":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Hydrateur":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Barman":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Epicier":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //     case "Ali Baba":
    //       this.collabInfos.push({pseudo: this.colorPerPseudo[i].pseudo, medalImage: "assets/icon/medal_icons/chef.png", description: this.colorPerPseudo[i].medal, toShow: false, color: this.colorPerPseudo[i].color});
    //       break;
    //
    //   }
    // }
  }

  setDescription(collab){
    this.collabInfos[this.collabInfos.indexOf(collab)] = {pseudo: collab.pseudo, medalImage: collab.medalImage, description: collab.description, toShow: !collab.toShow, color: collab.color};
  }

}
