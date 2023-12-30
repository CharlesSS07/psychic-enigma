import falcon
import falcon.asgi

class UserMessageEndpoint:

    async def on_websocket(self, req: falcon.Request, ws: falcon.asgi.WebSocket, account_id: str):
        
        try:
            await ws.accept()
        except WebSocketDisconnected as e:
            print(e)
            return
        
        messages = collections.deque()

        async def sink():
            while True:
                try:
                    message = await ws.receive_text()
                except falcon.WebSocketDisconnected:
                    break

                messages.append(message)

        sink_task = falcon.create_task(sink())

        while not sink_task.done():
            while ws.ready and not messages and not sink_task.done():
                await asyncio.sleep(0)

            try:
                await ws.send_text('echo: '+ account_id + ' ' + messages.popleft())
            except falcon.WebSocketDisconnected:
                break

        sink_task.cancel()
        try:
            await sink_task
        except asyncio.CancelledError:
            pass

app = falcon.asgi.App(
    # cors_enable=True # allows any endpoint to be accessed by the browser, could be insecure if that's not what we want
)
app.add_route('/users/{account_id}/messages', UserMessageEndpoint())

import posixpath

root_dir = posixpath.dirname(__file__)

static_dir = posixpath.join(root_dir, 'static')

app.add_static_route('/static/', static_dir)

