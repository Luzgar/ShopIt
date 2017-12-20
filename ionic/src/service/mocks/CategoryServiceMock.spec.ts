export class CategoryServiceMock{
    
    getArticle(query: string){

        return new Promise((resolve) => {
            resolve({count: 1, products:[{categories: "Boissons,Sodas,Sucre"}]});
        });
    }
}