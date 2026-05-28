describe('Testare Securitate Rute (Auth Guard)', () => {

  it('ar trebui să redirectioneze un vizitator neautentificat catre /login', () => {
    cy.visit('/ask');
    cy.url().should('include', '/login');
  });

  it('ar trebui sa permita accesul daca utilizatorul este logat', () => {
    cy.visit('/login');

    const mockUser = {
      id: 1,
      username: 'gregory_house',
      email: 'house@ppth.com',
      roles: ['USER'],
      score: 10,
      isBanned: false
    };

    //corecteaza contractul conform AuthResponseDTO
    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-jwt-token-12345',
        user: mockUser
      }
    }).as('loginRequest');

    cy.get('#username').type('gregory_house');
    cy.get('#password').type('vicodin_123');
    cy.get('.btn-submit').click();

    cy.wait('@loginRequest');
    cy.get('.btn-ask').click();

    cy.url().should('include', '/ask');
    cy.get('h2').should('contain.text', 'Pune o întrebare publică');
  });
});
