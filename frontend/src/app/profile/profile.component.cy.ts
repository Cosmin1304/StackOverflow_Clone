/// <reference types="cypress" />

import { ProfileComponent } from './profile.component';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';

describe('ProfileComponent - Testare de componenta', () => {

  //vizitatorul nelogat ar trebui sa fie dat afara de pe pagina
  it('ar trebui să redirecționeze către /login dacă nu există un utilizator curent', () => {
    const authServiceMock = {
      getCurrentUser: cy.stub().returns(null)//nu este nimeni logat
    };
    const navigateStub = cy.stub();
    const routerMock = { navigate: navigateStub };

    cy.mount(ProfileComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: UserService, useValue: {} },
        { provide: Router, useValue: routerMock }
      ]
    });

    //verifica daca a fost apelata redirectionarea in ngOnInit
    cy.wrap(navigateStub).should('have.been.calledWith', ['/login']);
  });

  //un utilizator logat ar trebui sa isi vada datele
  it('ar trebui să afișeze datele utilizatorului și să populeze formularul', () => {
    const mockUser = {
      id: 1,
      username: 'balumba',
      email: 'balumba@test.com',
      score: 42,
      isBanned: false
    };

    const authServiceMock = {
      getCurrentUser: cy.stub().returns(mockUser)
    };

    cy.mount(ProfileComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: UserService, useValue: {} },
        { provide: Router, useValue: { navigate: cy.stub() } }
      ]
    });

    //verifica scorul și statusul
    cy.get('.stats-box').should('contain.text', '42');
    cy.get('.stats-box').should('contain.text', 'Activ ✅');

    //verifica daca inputurile (ngModel) au preluat datele corecte
    cy.get('input[name="userName"]').should('have.value', 'balumba');
    cy.get('input[name="userEmail"]').should('have.value', 'balumba@test.com');
  });

  //testare functia de update
  it('ar trebui să apeleze UserService și să arate o alertă la actualizare cu succes', () => {
    const mockUser = { id: 1, username: 'vechi', email: 'vechi@test.com', score: 0, isBanned: false };
    const updatedUser = { id: 1, username: 'nume_nou', email: 'nou@test.com', score: 0, isBanned: false };

    const authServiceMock = {
      getCurrentUser: cy.stub().returns(mockUser)
    };

    //simularea unui raspuns de succes (200 OK) de la server
    const updateUserStub = cy.stub().returns(of(updatedUser));
    const userServiceMock = { updateUser: updateUserStub };

    cy.mount(ProfileComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: UserService, useValue: userServiceMock },
        { provide: Router, useValue: { navigate: cy.stub() } }
      ]
    });

    //stergem textul vechi si scriem text nou in input
    cy.get('input[name="userName"]').clear().type('nume_nou');
    cy.get('input[name="userEmail"]').clear().type('nou@test.com');
    cy.get('input[name="userPassword"]').type('parola123');

    //interceptarea alertei
    cy.on('window:alert', (text) => {
      expect(text).to.contains('Profil actualizat cu succes!');
    });

    //simuleaza apasarea butonului de summit
    cy.get('.btn-submit').click();

    //verifica daca serviciul a fost apelat cu id-ul corect si datele din formular.
    //Cheile reflecta UserRequestDTO actual: { username, email, password }.
    cy.wrap(updateUserStub).should('have.been.calledWith', 1, {
      username: 'nume_nou',
      email: 'nou@test.com',
      password: 'parola123'
    });
  });

  //testarea functiei de logout direct din componenta
  it('ar trebui să apeleze logout și să navigheze spre /login la delogare', () => {
    const mockUser = { id: 1, username: 'cosmin', email: 'cosmo@test.com' };

    const authServiceMock = {
      getCurrentUser: cy.stub().returns(mockUser),
      logout: cy.stub().as('logoutStub')
    };

    const routerMock = {
      navigate: cy.stub().as('navigateStub')
    };

    cy.mount(ProfileComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: UserService, useValue: {} },
        { provide: Router, useValue: routerMock }
      ]
    });

    cy.get('.btn-logout').click();

    cy.get('@logoutStub').should('have.been.called');
    cy.get('@navigateStub').should('have.been.calledWith', ['/login']);
  });

});
