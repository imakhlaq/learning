const child = require('child_process')

// to execute a command in the terminal using code
child.exec('ls', (err, data) => console.log({ err, data }))

child.exec('node 06_child_process', (err, data) => console.log({ err, data }))

//to start a process

const qApp = child.spawn('node', ['01_node_Intro.js'])

//child process sending data
qApp.stdout.on('data', data => {})

//sending data to child process
qApp.stdin.write('HUUuu')

qApp.on('close', () => {
  console.log('process closed')
})