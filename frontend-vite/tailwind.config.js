/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Jost', 'sans-serif'], // Altere a fonte padrão para Jost
      },
      colors: {
        'custom-gray': '#E3E3E3', // Sua cor personalizada
      },
    },
  },
  plugins: [],
}
