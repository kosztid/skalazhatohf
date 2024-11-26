# Az alapképet választjuk a hivatalos Node.js képek közül
FROM node:18

# Beállítjuk a munkakönyvtárat a konténerben
WORKDIR /usr/src/app

# A package.json és package-lock.json fájlokat a konténerbe másoljuk
COPY package*.json ./

# Telepítjük a függőségeket
RUN npm install

# Az alkalmazás fájljait átmásoljuk a konténerbe
COPY . .

# A konténeren belül elérhetővé tesszük az alkalmazás futtatási portját
EXPOSE 3000

# Az alkalmazás indításához szükséges parancs
CMD ["node", "src/index.js"]
