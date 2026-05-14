/// <reference types="cypress" />

import { LoginComponent } from './login.component';
import { AuthService } from '../services/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('LoginComponent - Testare de componenta', () => {

  it('ar trebui să randeze corect formularul de login', () => {
    cy.mount(LoginComponent, {
      providers: [
        { provide: AuthService, useValue: { login: cy.stub() } },
        { provide: Router, useValue: { navigate: cy.stub() } },
        { provide: ActivatedRoute, useValue: {} }
      ]
    });

    cy.get('h2').should('contain.text', 'Autentificare');
    cy.get('#username').should('be.visible');
    cy.get('#password').should('be.visible');
    cy.get('.btn-submit').should('contain.text', 'Log in');
  });

  it('ar trebui să afișeze o alertă dacă datele sunt incorecte', () => {
    const authServiceMock = {
      login: cy.stub().returns(throwError(() => new Error('Date invalide')))
    };

    cy.mount(LoginComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: { navigate: cy.stub() } },
        { provide: ActivatedRoute, useValue: {} }
      ]
    });

    cy.on('window:alert', (text) => {
      expect(text).to.contains('Eroare la autentificare');
    });

    cy.get('#username').type('user_gresit');
    cy.get('#password').type('parola_gresita');
    cy.get('.btn-submit').click();

    cy.wrap(authServiceMock.login).should('have.been.calledWith', {
      username: 'user_gresit',
      password: 'parola_gresita'
    });
  });

  it('ar trebui să navigheze către / la o logare cu succes', () => {
    const mockUser = { id: 1, username: 'albert', email: 'al@test.com', roles: [], score: 0, isBanned: false };

    const authServiceMock = {
      login: cy.stub().returns(of(mockUser))
    };

    const routerMock = {
      navigate: cy.stub().as('routerNavigate')
    };

    cy.mount(LoginComponent, {
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: {} }
      ]
    });

    cy.get('#username').type('albert');
    cy.get('#password').type('parola_corecta');
    cy.get('.btn-submit').click();

    cy.get('@routerNavigate').should('have.been.calledWith', ['/']);
  });

});
