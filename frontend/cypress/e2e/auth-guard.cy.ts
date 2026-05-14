describe('Testare Securitate Rute (Auth Guard)', () => {

  it('ar trebui să redirecționeze un vizitator neautentificat către /login', () => {
    //incearca accesare directa url protejat
    cy.visit('/ask');

    //guardul ar trebui sa intervina instant si sa faca trimiterea la login
    cy.url().should('include', '/login');
  });

  it('ar trebui să permită accesul dacă utilizatorul este logat', () => {
    //viziteaza pagina de login(care este publica si nu face redirect)
    cy.visit('/login');

    //intercepteaza apelul de login pentru a nu depinde de backend
    const mockUser = { id: 1, username: 'alonso', email: 'alonso@test.com', roles: [], score: 0, isBanned: false };
    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 200,
      body: mockUser
    }).as('loginRequest');

    //simuleaza procesul de autentificare
    cy.get('#username').type('alonso');
    cy.get('#password').type('parola_test');
    cy.get('.btn-submit').click();

    //asteapta logarea si redirectionarea automata catre Home ('/')
    cy.wait('@loginRequest');

    //acum AuthService are datele setate, iar Navbar-ul a afișat butoanele de user
    //navigam spre /ask dand click pe buton (asta folosește router-ul Angular, nu serverul)
    cy.get('.btn-ask').click();

    //guard-ul client-side citeste acum cu succes localStorage-ul si permite accesul
    cy.url().should('include', '/ask');
    cy.get('h2').should('contain.text', 'Pune o întrebare publică');
  });

});
