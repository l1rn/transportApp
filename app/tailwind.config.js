module.exports = {
    content: [
        './index.html',
        './src/**/*.{js,jsx,ts,tsx,vue}',
    ],
    theme: {
        extend: {
            colors: {
                primary: '#3490dc',
                secondary: '#ffed4a',
            },
            fontFamily: {
                sans: ['Helvetica', 'Arial', 'sans-serif'],
            },
            spacing: {
                18: '4.5rem',
            },
        },
    },
    plugins: [],
}
