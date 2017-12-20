export class RecipeServiceMock{


  getListById(idList : number){
    return new Promise((resolve) => {
      resolve({"id":1,"name":"Courses Lundi","items":[]})});
  }

  addArticle(listId: number, article: any) {
    return new Promise((resolve) => {
      resolve({"id": 1, "item": {"name_item": "Carotte", "quantity": 1, "taken": false, "category": "Fruits et légumes"}})});
  }

  getAllCurrentTitleByUser(idUser : number) {
    return new Promise((resolve) => {
      resolve({title: "Courses Lundi", listId: 1})});
  }

  getAllCurrentTitleByUser() {
    return new Promise((resolve) => {
      resolve({title: "Paella", listId: 1})});
  }

  getRecipeByIngredients(listIngredients: string){
    return new Promise((resolve) => {
      resolve({title: "Paella", listId: 1})});
  }

  getRecipeById(idRecipe : number){
    return new Promise((resolve) => {
      resolve({"id":1,"name":"Recipe1","numberPerson":2,"ingredients":[{"item":"Carotte","number":1,"taken":false,"category":"Fruits et légumes","price":0.0}],"description":"desc"})});
  }

  addRecipe(name: string, number_person: number, description: string, listeIngredients: any){
    return new Promise((resolve) => {
      resolve({"name":"Recipe2","number_person":"2","description":"blabla","ingredients":[{"name_item":"Pain","quantity":1,"category":"Céréales et féculents","taken":false}]})});
  }
}
