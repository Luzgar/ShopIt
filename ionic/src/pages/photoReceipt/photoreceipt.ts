import {Component} from "@angular/core";
import {Camera} from 'ionic-native';
import {ReceiptPhotoService} from "../../service/ReceiptPhotoService";
import { Storage } from '@ionic/storage';

@Component({
  selector: 'photoreceipt',
  templateUrl: 'photoreceipt.html',
  providers: [ReceiptPhotoService]
})

export class PhotoReceipt {

  public base64Image: string;
  userId: number;

  constructor(public storage: Storage, public receiptPhotoService: ReceiptPhotoService) {
    this.storage.get("userId").then((data) => {
      if(data != null){
        this.userId = data;
      }
      else {
        this.userId = -1;
      }
    });
  }

  takePicture(){

    Camera.getPicture({
      destinationType: Camera.DestinationType.DATA_URL,
      targetWidth: 1000,
      targetHeight: 1000
    }).then((imageData) => {
      // imageData is a base64 encoded string
      this.base64Image = "data:image/jpeg;base64," + imageData;
    }, (err) => {
      console.log(err);
    });
  }

  sendPicture(){
    if(this.base64Image != null)
      this.receiptPhotoService.sendReceiptPhoto(this.base64Image, this.userId);
    else
      alert('Vous n\'avez pas pris encore de photo');
  }
}
