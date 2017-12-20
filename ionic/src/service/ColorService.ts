import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class ColorService{


  //colorList = ["wisteria", "alizarin", "midnight_blue", "sun_flower"]

  colorList =  [{colorName: "wisteria", colorHexa: "#8e44ad"},
                {colorName: "alizarin", colorHexa: "#e74c3c"},
                {colorName: "midnight_blue", colorHexa: "#2c3e50"},
                {colorName: "sun_flower", colorHexa: "#f1c40f"}]

  constructor(private http: Http){
  }

  getAllColorName(){
    var array = [];
    for(var i = 0; i < this.colorList.length; ++i){
      array.push(this.colorList[i].colorName);
    }
    return array;
  }

  getHexaByName(colorName: string){
    for(var i = 0; i < this.colorList.length; ++i){
      if(colorName == this.colorList[i].colorName)
        return this.colorList[i].colorHexa;
    }
  }

}
