const fs = require('fs')

//readable streams
const readStream = fs.createReadStream('./earth.txt', { encoding: 'UTF-8' })

readStream.on('data', (err, content) => {})

readStream.on('end', () => {})

//write stream
const writeStream = fs.createWriteStream('./earth.txt')

writeStream.write('hu[[[aa')
writeStream.write('dadadadad')

//pipe
readStream.pipe(writeStream)//it will read from readStream and write to writeStream