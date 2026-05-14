/// <reference types="cypress" />

import { NavbarComponent } from './navbar.component';
import { AuthService } from '../services/auth.service';
import { SearchService } from '../services/search.service';
import { UserService } from '../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

describe('NavbarComponent - Testare de componenta', () => {

  beforeEach(() => {
    cy.viewport(1200, 800); //seteaza un ecran de dimensiune desktop
  });

  it('ar trebui să afișeze butoanele de Login și Sign up pentru vizitatori (nelogat)', () => {
    //simuleaza starea de nelogat
    const authServiceMock = {
      isLoggedIn$: new BehaviorSubject<boolean>(false).asObservable(),
      getCurrentUser: cy.stub().returns(null)
    };

    cy.mount(NavbarComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SearchService, useValue: { changeSearchTerm: cy.stub() } },
        { provide: UserService, useValue: {} },
        { provide: Router, useValue: {} },
        { provide: ActivatedRoute, useValue: {} } //necesar pentru routerLink
      ]
    });

    //verifica ca elementele pentru guests sunt vizibile
    cy.get('.btn-login').should('be.visible').and('contain.text', 'Log in');
    cy.get('.btn-register').should('be.visible').and('contain.text', 'Sign up');
    cy.get('.btn-ask').should('not.exist'); // Vizitatorii nu au buton de Ask
  });

  it('ar trebui să afișeze meniul complet pentru utilizatorul logat', () => {
    //simuleaza starea de logat și un utilizator
    const mockUser = { id: 1, username: 'alex_dev' };
    const authServiceMock = {
      isLoggedIn$: new BehaviorSubject<boolean>(true).asObservable(),
      getCurrentUser: cy.stub().returns(mockUser)
    };

    cy.mount(NavbarComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SearchService, useValue: { changeSearchTerm: cy.stub() } },
        { provide: UserService, useValue: {} },
        { provide: Router, useValue: {} },
        { provide: ActivatedRoute, useValue: {} }
      ]
    });

    //verifica interfata protejata
    cy.get('.user-logged').should('be.visible').and('contain.text', 'alex_dev');
    cy.get('.btn-ask').should('be.visible');
    cy.get('.btn-logout').contains('Log out').should('be.visible');
    cy.get('.btn-login').should('not.exist'); //login-ul dispare
  });

  it('ar trebui să trimită textul căutat către SearchService în timp real', () => {
    //prinde apelul functiei din SearchService
    const searchServiceMock = {
      changeSearchTerm: cy.stub().as('searchStub')
    };

    const authServiceMock = {
      isLoggedIn$: new BehaviorSubject<boolean>(false).asObservable(),
      getCurrentUser: cy.stub().returns(null)
    };

    cy.mount(NavbarComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SearchService, useValue: searchServiceMock },
        { provide: UserService, useValue: {} },
        { provide: Router, useValue: {} },
        { provide: ActivatedRoute, useValue: {} }
      ]
    });

    //scrie in bara de cautare
    cy.get('.search-bar input').type('Angular');

    //verifica daca serviciul a primit exact valoarea pe care am tastat-o
    cy.get('@searchStub').should('have.been.calledWith', 'Angular');
  });

  it('ar trebui să delogheze utilizatorul și să navigheze spre /login la click pe Log out', () => {
    //pregateste stubs pentru logout și navigare
    const authServiceMock = {
      isLoggedIn$: new BehaviorSubject<boolean>(true).asObservable(),
      getCurrentUser: cy.stub().returns({ username: 'test' }),
      logout: cy.stub().as('logoutStub')
    };

    const routerMock = {
      navigate: cy.stub().as('navigateStub')
    };

    cy.mount(NavbarComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SearchService, useValue: {} },
        { provide: UserService, useValue: {} },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: {} }
      ]
    });

    //apasam click pe butonul de "Log out"
    cy.get('.btn-logout').contains('Log out').click();

    //verifica logica de delogare
    cy.get('@logoutStub').should('have.been.called');
    cy.get('@navigateStub').should('have.been.calledWith', ['/login']);
  });

});
