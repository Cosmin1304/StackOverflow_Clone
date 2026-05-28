describe('Flux Înregistrare - Reguli de Business', () => {
  it('ar trebui sa respinga un username prea scurt direct din backend', () => {
    cy.visit('/register');

    //introduce 4 caractere (trece de Angular minlength="3", dar pica în Java la min 5)
    cy.get('input[name="username"]').type('greg');
    cy.get('input[name="email"]').type('greg@ppth.com');
    cy.get('input[name="phoneNumber"]').type('+40712345678');
    cy.get('input[name="password"]').type('ParolaCorecta123!');

    //acum butonul va fi activ si Cypress va putea da click
    cy.get('.btn-submit').click();

    //verifica ca am prins eroarea trimisa de Java (IllegalArgumentException)
    cy.on('window:alert', (text) => {
      expect(text).to.contains('A apărut o eroare la crearea contului');
    });

    //ramane pe pagina de register, nu trecem mai departe
    cy.url().should('include', '/register');
  });
});
