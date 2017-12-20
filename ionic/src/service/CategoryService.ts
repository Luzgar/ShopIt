import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class CategoryService{

  constructor(private http: Http){
  }

  getArticle(query: string){
    return this.http.get("https://world.openfoodfacts.org/cgi/search.pl?search_terms="+query+"&search_simple=1&action=process&lang=fr&json=1")
      .map((res:Response) => res.json());
  }

  getIconName(categoryName: string): string {
    switch(categoryName){
      case 'Boissons':
        return 'coke.png'
      case 'Poissons':
        return  'fishing.png'
      case 'Surgelés':
        return 'snow-flake-icon.png'
      case 'Viandes':
        return 'steak.png'
    }
  }

}
