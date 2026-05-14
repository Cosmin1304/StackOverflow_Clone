import { defineConfig } from "cypress";

export default defineConfig({
  allowCypressEnv: false,

  component: {
    devServer: {
      framework: "angular",
      bundler: "webpack",
    },
    specPattern: "**/*.cy.ts",
  },

  e2e: {
    baseUrl: 'http://localhost:4200', // Adresa unde rulează "ng serve"
    setupNodeEvents(on, config) {
    },
    specPattern: 'cypress/e2e/**/*.cy.ts', // Unde va căuta testele E2E
  },
});
