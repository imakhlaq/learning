const events = require('events')

const pizzaEvent = new events.EventEmitter()

pizzaEvent.on('ready', name => console.log(`${name} pizza is ready`))

setTimeout(() => pizzaEvent.emit('ready', 'non veg'), 2000)