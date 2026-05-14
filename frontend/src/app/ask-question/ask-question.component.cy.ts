/// <reference types="cypress" />

import { AskQuestionComponent } from './ask-question.component';
import { Router } from '@angular/router';

describe('AskQuestionComponent', () => {

  it('ar trebui să randeze corect formularul', () => {
    //montam componenta, inject(Router) -> trebuie sa dam un mock (o copie)
    cy.mount(AskQuestionComponent, {
      providers: [
        { provide: Router, useValue: { navigate: cy.stub().as('routerNavigate') } }
      ]
    });

    //verifica daca titlul paginii exista
    cy.get('h2').should('contain.text', 'Pune o întrebare publică');

    //verifica daca toate inputurile sunt vizibile
    cy.get('#title').should('be.visible');
    cy.get('#body').should('be.visible');
    cy.get('#pictureUrl').should('be.visible');
    cy.get('#tags').should('be.visible');
  });

  it('ar trebui să afișeze o alertă și să navigheze la home la submit', () => {
    cy.mount(AskQuestionComponent, {
      providers: [
        { provide: Router, useValue: { navigate: cy.stub().as('routerNavigate') } }
      ]
    });

    //completeaza formularul simuland input de la tastatura
    cy.get('#title').type('Cum folosesc Cypress in Angular?');
    cy.get('#body').type('Am nevoie de un exemplu clar pentru component testing.');
    cy.get('#tags').type('angular, cypress, testing');

    //intercepteaza fereastra de alert() din browser pentru a verifica mesajul
    cy.on('window:alert', (text) => {
      expect(text).to.contains('salvată');
    });

    //simuleaza butonul
    cy.get('.btn-submit').click();

    //verifica apelarea router.navigate(['/'])
    cy.get('@routerNavigate').should('have.been.calledWith', ['/']);
  });
});
