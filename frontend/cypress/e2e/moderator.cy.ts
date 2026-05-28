describe('Flux de moderare', () => {

  const timestamp = Date.now();

  //definirea datelor pentru noul utilizator
  const userBanat = {
    username: `regula_${timestamp}`,
    email: `bad_${timestamp}@ppth.com`,
    password: 'ParolaCorecta123!',
    phone: '+40799999999'
  };

  //datele contului real de moderator
  const moderator = {
    username: 'moderator',
    password: 'moderator'
  };

  it('ar trebui sa baneze utilizatorul din panou si sa blocheze logarea', () => {

    //se asculta toate alertele din test
    cy.on('window:alert', (text) => {
      if (text.includes('succes')) {
        //alerta de la inregistrare
        expect(text).to.contains('Cont creat cu succes');
      } else if (text.includes('banat')) {
        //alerta de la logare blocata
        expect(text).to.contains('Contul tău a fost banat');
      }
    });

    //crearea utiliztorului care va fi banat
    cy.visit('/register');
    cy.get('input[name="username"]').type(userBanat.username);
    cy.get('input[name="email"]').type(userBanat.email);
    cy.get('input[name="phoneNumber"]').type(userBanat.phone);
    cy.get('input[name="password"]').type(userBanat.password);
    cy.get('.btn-submit').click();

    //se asteapta redirectionarea la login dupa inregistrare
    cy.url().should('include', '/login');

    //moderatorul baneaza userul
    cy.get('#username').type(moderator.username);
    cy.get('#password').type(moderator.password);
    cy.get('.btn-submit').click();

    //verificam ca moderatorul s-a logat cu succes
    cy.url().should('eq', Cypress.config().baseUrl + '/');

    //butonul care duce spre panoul de moderare
    cy.get('.navbar').contains('Moderator Panel').click();

    //verificam ca Angular Router ne-a mutat pe URL-ul corect fara sa reincarce pagina
    cy.url().should('include', '/moderator');

    // ========================================================
    //banarea contului
    cy.intercept('PUT', '**/api/users/*/ban-status*').as('banRequest'); // Interceptăm apelul de Ban către backend

    //cauta randul (tr) care contine numele utilizatorului si apasa butonul cu clasa .btn-ban
    cy.contains('tr', userBanat.username)
      .find('.btn-ban')
      .should('be.visible')
      .click();

    //aștepta ca Spring Boot sa proceseze ban-ul in baza de date
    cy.wait('@banRequest').its('response.statusCode').should('eq', 200);

    //delogarea adminului
    cy.get('.navbar').contains('Log out').click();

    //utilizatorul incearca sa se logheze
    cy.url().should('include', '/login');

    //introduce datele utilizatorului banat
    cy.get('#username').type(userBanat.username);
    cy.get('#password').type(userBanat.password);

    //intercepteaza cererea ca sa valideze raspunsul backendului
    cy.intercept('POST', '**/api/auth/login').as('loginAttempt');

    cy.get('.btn-submit').click();

    //verifica JwtAuthenticationFilter a returnat 403 Forbidden
    cy.wait('@loginAttempt').its('response.statusCode').should('eq', 403);

    //verificam ca Angular a respectat raspunsul si nu a schimbat pagina
    cy.url().should('include', '/login');
  });
});
