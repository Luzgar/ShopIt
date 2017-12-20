import { Network } from '@ionic-native/network';
import { RepartitionServiceMock } from './../../service/mocks/RepartitionServiceMock.spec';
import { RepartitionService } from './../../service/RepartitionService';
import { ColorService } from './../../service/ColorService';
import { CategoryServiceMock } from './../../service/mocks/CategoryServiceMock.spec';
import { CategoryService } from './../../service/CategoryService';
import { ListServiceMock } from './../../service/mocks/ListServiceMock.spec';
import { ListService } from './../../service/ListService';
import { ShopListPage, KeysPipe } from './shopList';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By }           from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { IonicModule, Platform, NavController, NavParams} from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { PlatformMock, StatusBarMock, SplashScreenMock } from '../../../test-config/mocks-ionic';
import {HttpModule} from "@angular/http";
import {RealTimeService} from "../../service/RealTimeService";
import {IonicStorageModule} from "@ionic/storage";
import {equal} from "assert";

class MockNavParams{
  data = {
      nameShopList : "COCO",
      listId : -2
  };

  get(param){
    return this.data[param];
  }
}


describe('ShopListPage', function() {
  let de: DebugElement;
  let comp: ShopListPage;
  let fixture: ComponentFixture<ShopListPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShopListPage, KeysPipe],
      imports: [
        IonicModule.forRoot(ShopListPage),
        IonicStorageModule.forRoot(ShopListPage),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        { provide: Platform, useClass: PlatformMock},
        { provide: NavParams, useClass: MockNavParams},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        { provide: CategoryService, useClass: CategoryServiceMock },
        { provide: ListService, useClass: ListServiceMock },
        { provide: RepartitionService, useClass: RepartitionServiceMock }, //RAJOUTER UN MOCK + MODIFIER LES MOCKS EXISTANTS EN RAJOUTANT LES NEW METHODES
        ColorService,
        Network
      ]
    });
  }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ShopListPage);
        comp = fixture.componentInstance;
        de = fixture.debugElement.query(By.css('h3'));
        var itemList: Array<{name_item: string, quantity: number, category: string, taken: boolean, idOwner: number, isOwned: boolean, color: string, price: number}>;
        var item1 = {name_item: "Coca", quantity: 1, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 2};
        var item2 = {name_item: "Sprite", quantity: 3, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 5};
        var item3 = {name_item: "Steak", quantity: 2, category: "Viandes", taken: true, idOwner: -1, isOwned: false, color: "red", price: 8};

        itemList = [];
        itemList.push(item1);
        itemList.push(item2);
        itemList.push(item3);
        comp.itemList = itemList;
        var dividedList = {"Boissons": [item1, item2], "Viandes": [item3]};
        comp.dividedList = dividedList;
    });

    it('should create component', () => expect(comp).toBeDefined());

    it('should expulse user', () => {
        var list = {"id": 0, "name": "hoy", "owner": { "id": 0,"login": "bla","pswd": "rebla", "pseudo": "rerebla"},"elements":[{"item": "butter","number": 1,"taken": false,"category": "fresh product"}],"contributors":[{"id": 3,"login": "bla","pswd": "rebla","pseudo": "rerebla"}]}
        comp.userId = 2;
        expect(comp.isUserExpulsed(list.contributors, list.owner)).toBeTruthy();
    });

    it('should not expulse user', () => {
        var list = {"id": 0, "name": "hoy", "owner": { "id": 0,"login": "bla","pswd": "rebla", "pseudo": "rerebla"},"elements":[{"item": "butter","number": 1,"taken": false,"category": "fresh product"}],"contributors":[{"id": 3,"login": "bla","pswd": "rebla","pseudo": "rerebla"}]}
        comp.userId = 3;
        expect(comp.isUserExpulsed(list.contributors, list.owner)).toBeFalsy();

        comp.userId = 0;
        expect(comp.isUserExpulsed(list.contributors, list.owner)).toBeFalsy();
    });

    it('should parse an itemlist into specific JSON format', () => {
        var item1 = {name_item: "Coca", quantity: 1, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 2};
        var item2 = {name_item: "Sprite", quantity: 3, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 5};
        var item3 = {name_item: "Steak", quantity: 2, category: "Viandes", taken: true, idOwner: -1, isOwned: false, color: "red", price: 8};
        
        comp.divideList();
        var expectedResult = {"Boissons": [item1, item2], "Viandes": [item3]};
        expect(comp.dividedList).toEqual(expectedResult);
    });

    it('should attribute a Color per user in a list', () => {
        var list = {"id": 0, "name": "hoy", "owner": { "id": 0,"login": "bla","pswd": "rebla", "pseudo": "jojo"},"elements":[{"item": "butter","number": 1,"taken": false,"category": "fresh product"}],"contributors":[{"id": 3,"login": "bla","pswd": "rebla","pseudo": "gege"}]}
        comp.userId = 0;
        comp.attributeColorPerContributor(list.contributors, list.owner);
        var expectedResult = {0 : {pseudo: "jojo", color: "wisteria"}, 3: {pseudo: "gege", color: "alizarin"}};
        expect(comp.color).toBe("wisteria");
        expect(comp.userPseudo).toBe("jojo");
        expect(comp.colorPerContributor).toEqual(expectedResult);
        
        comp.userId = 3;
        comp.attributeColorPerContributor(list.contributors, list.owner);
        expect(comp.color).toBe("alizarin");
        expect(comp.userPseudo).toBe("gege");
        expect(comp.colorPerContributor).toEqual(expectedResult);

    });

    

    it('should delete an item from a list', () => {
        comp.deleteItem(comp.itemList[0], "Boissons");
        
        var item2 = {name_item: "Sprite", quantity: 3, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 5};
        var item3 = {name_item: "Steak", quantity: 2, category: "Viandes", taken: true, idOwner: -1, isOwned: false, color: "red", price: 8};
        var expectedResult = [];
        expectedResult.push(item2);
        expectedResult.push(item3);
        expect(comp.itemList.length).toBe(2);
        expect(comp.itemList).toEqual(expectedResult);
        
        var expectedDividedResult = {"Boissons": [item2], "Viandes": [item3]};
        expect(comp.dividedList).toEqual(expectedDividedResult);

    });

    /*it('should add an item into a list', () => {
        var item = "Pizza Royale";
        comp.network.type = "4g";
        comp.addItem(item).then(() => {
            console.log("ok");
        });
    });
    
    it('should reserve an item from a list', () => {

    });

    it('should set taken an item from a list', () => {

    });
    
    it('should add a quantity to an item from a list', () => {
        var item1 = {name_item: "Coca", quantity: 2, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 2};
        var item2 = {name_item: "Sprite", quantity: 3, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 5};
        var item3 = {name_item: "Steak", quantity: 3, category: "Viandes", taken: true, idOwner: -1, isOwned: false, color: "red", price: 8};

        comp.addItem(comp.dividedList['Boissons'][0]);
        comp.addItem(comp.dividedList['Viandes'][0]);
        var expectedResult = {"Boissons": [item1, item2], "Viandes": [item3]};
        expect(comp.dividedList).toEqual(expectedResult);
    });

    it('should remove a quantity to an item from a list', () => {
    
    });

    */

    it('should calculate the total price of a list', () => {
        comp.getTotalPrice(); 
        expect(comp.totalPrice).toBe(33); // Par rapport à la liste dans le beforeEach

        var item1 = {name_item: "Coca", quantity: 6, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 4};
        var item2 = {name_item: "Sprite", quantity: 5, category: "Boissons", taken: false, idOwner: -1, isOwned: false, color: "blue", price: 10.5};
        comp.itemList = [];
        comp.itemList.push(item1);
        comp.itemList.push(item2);

        comp.getTotalPrice();
        expect(comp.totalPrice).toBe(76.5);

    });

});