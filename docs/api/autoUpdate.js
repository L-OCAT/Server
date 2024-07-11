const fs = require('fs');
const path = require('path');
const yaml = require('js-yaml');

const STANDARD_CHARSET = 'UTF-8';

const baseDirectory = process.env.GITHUB_WORKSPACE || path.resolve(__dirname, '../..');
const apiYamlDirectory = path.join(baseDirectory, 'docs/api');
const swaggerPath = path.join(baseDirectory, 'src/main/resources/dist/swagger-initializer.js');

function readFile(filePath, encoding = STANDARD_CHARSET) {
    try {
        return fs.readFileSync(filePath, encoding);
    } catch (error) {
        console.error(`Error reading file at ${filePath}:`, error);
        process.exit(1);
    }
}

function readDirectory(directoryPath) {
    try {
        return fs.readdirSync(directoryPath);
    } catch (error) {
        console.error(`Error reading directory at ${directoryPath}:`, error);
        process.exit(1);
    }
}

const urlsRegex = /urls: \[(.*?)]/s;
let swaggerContent = readFile(swaggerPath);
const urlsMatch = swaggerContent.match(urlsRegex);
let existingUrls = [];

if (urlsMatch && urlsMatch[1]) {
    existingUrls = JSON.parse(`[${urlsMatch[1].replace(/url: /g, '"url": ').replace(/name: /g, '"name": ')}]`);
}

let yamlFiles = readDirectory(apiYamlDirectory).filter(file => file.endsWith('.yaml'));
yamlFiles.forEach(file => {
    const filePath = path.join(apiYamlDirectory, file);
    const fileContent = readFile(filePath);
    const parsedYaml = yaml.load(fileContent);

    const title = parsedYaml.info?.title || path.basename(file, '.yaml');
    const url = `./docs/api/${file}`;

    if (!existingUrls.some(existingUrl => existingUrl.url === url)) {
        existingUrls.push({ url, name: title });
    }
});

const newUrlsString = existingUrls.map(url => `{url: "${url.url}", name: "${url.name}"}`).join(', ');
swaggerContent = swaggerContent.replace(urlsRegex, `urls: [${newUrlsString}]`);

try {
    fs.writeFileSync(swaggerPath, swaggerContent, 'utf-8');
    console.log('API 문서 최신화 완료!');
} catch (error) {
    console.error(`Error writing swagger-initializer.js :`, error);
    process.exit(1);
}
