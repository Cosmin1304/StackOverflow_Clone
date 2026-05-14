/// <reference types="cypress" />

import { RegisterComponent } from './register.component';
import { UserService } from '../services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';


describe('RegisterComponent - Testare de Componentă', () => {

  //definim stuburile
  let registerUserStub: any;
  let navigateStub: any;

  beforeEach(() => {
    registerUserStub = cy.stub();
    navigateStub = cy.stub();
  });

  const setupComponent = () => {
    cy.mount(RegisterComponent, {
      providers: [
        { provide: UserService, useValue: { registerUser: registerUserStub } },
        { provide: Router, useValue: { navigate: navigateStub } },
        { provide: ActivatedRoute, useValue: {} } // Necesar pentru routerLink-ul spre login
      ]
    });
  };

  it('ar trebui să randeze formularul cu butonul de înregistrare dezactivat inițial', () => {
    setupComponent();

    cy.get('h2').should('contain.text', 'Creare Cont Nou');
    //butonul ar trebui sa fie disabled conform logic [disabled]="registerForm.invalid"
    cy.get('.btn-submit').should('be.disabled');
  });

  it('ar trebui să afișeze mesaje de eroare pentru input-uri invalide', () => {
    setupComponent();

    //numele de utilizator este prea scurt (sub 3 caractere)
    cy.get('#username').type('al').blur(); // blur() simuleaza "touched"
    cy.get('.error-message').should('contain.text', 'minim 3 caractere');

    //email invalid
    cy.get('#email').type('email-gresit').blur();
    cy.get('.error-message').should('contain.text', 'email valid');

    //parola prea scurta (sub 6 caractere)
    cy.get('#password').type('123').blur();
    cy.get('.error-message').should('contain.text', 'minim 6 caractere');

    cy.get('.btn-submit').should('be.disabled');
  });

  it('ar trebui să permită înregistrarea și să navigheze la /login la succes', () => {
    //simularea unui raspuns de succes de la server
    const mockUser = { id: 1, username: 'cosmin_dev', email: 'cosmo@test.ro' };
    registerUserStub.returns(of(mockUser));

    setupComponent();

    //completam datele corect
    cy.get('#username').type('cosmin_dev');
    cy.get('#email').type('cosmo@test.ro');
    cy.get('#password').type('parola123');

    //butonul ar trebui sa fie acum activ
    cy.get('.btn-submit').should('not.be.disabled').click();

    //verificarea apelului corect al serviciului
    cy.wrap(registerUserStub).should('have.been.calledWith', {
      username: 'cosmin_dev',
      email: 'cosmo@test.ro',
      password: 'parola123'
    });

    //verificarea navigarii spre login
    cy.wrap(navigateStub).should('have.been.calledWith', ['/login']);
  });

  it('ar trebui să afișeze o alertă dacă serverul returnează eroare', () => {
    //simularea unei eroari de la backend
    registerUserStub.returns(throwError(() => new Error('Conflict')));

    setupComponent();

    cy.get('#username').type('user_existent');
    cy.get('#email').type('test@test.ro');
    cy.get('#password').type('parola123');

    //interceptia ferestrei de alerta
    cy.on('window:alert', (text) => {
      expect(text).to.contains('A apărut o eroare la crearea contului');
    });

    cy.get('.btn-submit').click();
  });

});
