import falcon.asgi

import collections

import traceback

class UserMessageEndpoint:
    
    async def on_get(self, req, resp):
        pass

    async def on_websocket(self, req: falcon.asgi.Request, ws: falcon.asgi.WebSocket):
        print('on_websocket called')
        try:
            await ws.accept()
        except WebSocketDisconnected as e:
            print(e)
            return
        print('0')
        messages = collections.deque()
        print('0.25')
        async def sink():
            while True:
                try:
                    print('0.5')
                    message = await ws.receive_media()
                    print(message)
                    messages.append(message)
                except Exception:
                    traceback.print_exc()
                    break
                except falcon.WebSocketDisconnected as e:
                    break

        sink_task = falcon.create_task(sink())
        print('1')
        while not sink_task.done():
            while ws.ready and not messages and not sink_task.done():
                await asyncio.sleep(0)

            try:
                await ws.send_text('echo: '+  messages.popleft())
            except falcon.WebSocketDisconnected as e:
                traceback.print_exc()
                break

        sink_task.cancel()
        try:
            await sink_task
        except asyncio.CancelledError:
            pass

app = falcon.asgi.App(
    # cors_enable=True # allows any endpoint to be accessed by the browser, could be insecure if that's not what we want
)
app.add_route('/usersmessages', UserMessageEndpoint())

import posixpath

root_dir = posixpath.dirname(__file__)

static_dir = posixpath.join(root_dir, 'static')

app.add_static_route('/static/', static_dir)

