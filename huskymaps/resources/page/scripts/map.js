/**
  * Map project javascript file written for CS61B/CS61BL.
  * This is not an example of good javascript or programming practice.
  * Feel free to improve this front-end for your own personal pleasure.
  * Authors: Alan Yao (Spring 2016), Colby Guan (Spring 2017), Alexander Hwang (Spring 2018), Eli Lipsitz (Spring 2019)
  * If using, please credit authors.
  **/

(function() {
    window.addEventListener('DOMContentLoaded', setup);

    /* ══════════════════════════════════ ೋღ PROPERTIES ღೋ ════════════════════════════════ */
    let mapBody;
    let routeStatus;
    let loadingStatus;
    let errorStatus;
    let warningsContainer;
    let directionsText;
    let routingCanvas;
    let themeableElements = ['body', '#actions', '.card', '#search',
                                '.status', '.settings', '#clear', '.action-icon'];
    const SAFE_WIDTH = 1120;
    const SAFE_HEIGHT = 800;
    // psuedo-lock
    let getInProgress = false;
    let updatePending = false;
    let route_params = {};
    let map;
    let dest;
    let path;
    let markers = [];
    let ullon_bound, ullat_bound, lrlon_bound, lrlat_bound;
    let constrain;
    let width;
    let height;

    const base_move_delta = 64;
    const MAX_DEPTH = 7;
    const MIN_DEPTH = 0; // Depth limits based on pulled data
    const START_LAT = 47.6553;
    const START_LON = -122.3035;
    const START_DEPTH = 3;
    const ROOT_ULLAT = 47.754097979680026;
    const ROOT_ULLON = -122.6953125;
    const ROOT_LRLAT = 47.51720069783939;
    const ROOT_LRLON = -121.9921875;
    const NUM_X_TILES_AT_DEPTH = [2, 4, 8, 16, 32, 64, 128, 256];
    const NUM_Y_TILES_AT_DEPTH = [1, 2, 4,  8, 16, 32,  64, 128];

    let depth;
    let lat;
    let lon;

    /* Set server URIs */
    const RASTER_SERVER = document.location.origin + '/raster';
    const ROUTE_SERVER = document.location.origin + '/route';
    const SEARCH_SERVER = document.location.origin + '/search';

    /* ════════════════════════════ ೋღ HELPERS ღೋ ══════════════════════════ */
    /* get_londpp is offset by 1 since lon is twice as wide. */
    function get_londpp() { return (ROOT_LRLON - ROOT_ULLON) / 256 / NUM_X_TILES_AT_DEPTH[depth]; }
    /* get_latdpp is negative. */
    function get_latdpp() { return (ROOT_LRLAT - ROOT_ULLAT) / 256 / NUM_Y_TILES_AT_DEPTH[depth]; }

    function get_view_bounds() {
        return {
            ullat: lat - (0.5 * height * get_latdpp()),
            ullon: lon - (0.5 * width * get_londpp()),
            lrlat: lat + (0.5 * height * get_latdpp()),
            lrlon: lon + (0.5 * width * get_londpp()),
            depth: depth
        }
    }

    function removeMarkers() {
        document.getElementById('markers').innerHTML = '';
        markers = [];
    }

    function updateImg() {
        if (getInProgress) {
            updatePending = true;
            return;
        }

        show(loadingStatus);
        getInProgress = true;
        let params = get_view_bounds();
        warningsContainer.innerHTML = "";

        fetch(RASTER_SERVER + createUrlParams(params))
            .then(checkStatus)
            .then(handleRasterSuccess)
            .catch(handleRasterError);
    }

    function handleRasterSuccess(response) {
        let data = JSON.parse(response);
        if (data.success) {
            hide(loadingStatus);
            map.src = 'data:image/png;base64,' + data.image;
            routingCanvas.width = map.width;
            routingCanvas.height = map.height;
            ullon_bound = data.ullon;
            ullat_bound = data.ullat;
            lrlon_bound = data.lrlon;
            lrlat_bound = data.lrlat;
            getInProgress = false;

            updateT();
            drawRoute();
            if (updatePending) {
                updatePending = false;
                updateImg();
            }
        } else {
            hide(loadingStatus);
        }
    }

    function handleRasterError(response) {
        console.error(response);
        getInProgress = false;
        show(errorStatus);
        setTimeout(function() {
            hide(errorStatus);
        }, 4000);
    }

    function updateT() {
        let londpp = get_londpp();
        let latdpp = get_latdpp();
        let computed = get_view_bounds();
        let tx = (ullon_bound - computed.ullon) / londpp;
        let ty = (ullat_bound - computed.ullat) / latdpp;

        let newHash = "lat=" + lat + "&lon=" + lon + "&depth=" + depth;
        history.replaceState(null, null, document.location.pathname + '#' + newHash);

        setPosition(map, tx, ty);
        setPosition(routingCanvas, tx, ty);
        for (let i = 0; i < markers.length; i++) {
            const marker = markers[i];
            const marker_tx = (marker.lon - computed.ullon) / londpp  - marker.element.offsetWidth / 2;
            const marker_ty = (marker.lat - computed.ullat) / latdpp - marker.element.offsetHeight / 2;
            setPosition(marker.element, marker_tx, marker_ty);
        }

        if (route_params.end_lat) {
            let dest_x = (route_params.end_lon - computed.ullon) / londpp - 12.5;
            let dest_y = (route_params.end_lat - computed.ullat) / latdpp - 25;
            setPosition(dest, dest_x, dest_y);
        }
        // validate transform - true if img needs updating
        return computed.ullon < ullon_bound || computed.ullat > ullat_bound ||
            computed.lrlon > lrlon_bound || computed.lrlat < lrlat_bound;
    }

    function updateRoute() {
        fetch(ROUTE_SERVER + createUrlParams(route_params))
            .then(checkStatus)
            .then(handleRouteSuccess)
            .catch(console.err);
    }

    function handleRouteSuccess(response) {
        let data = JSON.parse(response);
        path = data.coordinates;
        updateImg();
        directionsText.innerHTML = data.directions;
    }

    function conditionalUpdate() {
        if (updateT()) {
            console.log('Update required.');
            updateImg();
        }
    }

    function drawRoute() {
        const graphics = routingCanvas.getContext('2d');
        graphics.clearRect(0, 0, routingCanvas.width, routingCanvas.height);
        if (!path) {
            return;
        }

        const xOffset = parseInt(routingCanvas.style.left, 10);
        const yOffset = parseInt(routingCanvas.style.top, 10);
        const longPerPixel = get_londpp();
        const latPerPixel = get_latdpp();
        const boundingBox = get_view_bounds();

        graphics.strokeStyle = '#407fff';
        graphics.lineWidth = 5;
        graphics.lineJoin = 'round';
        graphics.lineCap = 'round';
        graphics.beginPath();
        let x = (path[0].lon - boundingBox.ullon) / longPerPixel - xOffset;
        let y = (path[0].lat - boundingBox.ullat) / latPerPixel - yOffset;
        graphics.moveTo(x, y);
        for (let i = 1; i < path.length; i++) {
            x = (path[i].lon - boundingBox.ullon) / longPerPixel - xOffset;
            y = (path[i].lat - boundingBox.ullat) / latPerPixel - yOffset;
            graphics.lineTo(x, y);
        }
        graphics.stroke();
    }

    function zoom(delta) {
        depth += delta;
        updateImg();
    }

    function zoomIn() {
        if (depth === MAX_DEPTH) {
            return;
        }
        zoom(1);
    }

    function zoomOut() {
        if (depth === MIN_DEPTH) {
            return;
        }
        zoom(-1);
    }

    function handleDimensionChange() {
        width = window.innerWidth;
        height = window.innerHeight;
        updateT();
    }

    function updateConstrain() {
        if (constrain) {
            mapBody.style.maxHeight = SAFE_HEIGHT + "px";
            mapBody.style.maxWidth = SAFE_WIDTH + "px";
        } else {
            mapBody.style.maxHeight = "";
            mapBody.style.maxWidth = "";
        }
        handleDimensionChange();
    }

    function handleHashParameters() {
        // https://stackoverflow.com/a/2880929/437550
        let hash = window.location.hash.substring(1).split('&');
        for (let i = 0; i < hash.length; i += 1) {
            let temp = hash[i].split('=');

            if (temp[0] === 'lat') {
                lat = parseFloat(temp[1]);
            } else if (temp[0] === 'lon') {
                lon = parseFloat(temp[1]);
            } else if (temp[0] === 'depth') {
                depth = parseInt(temp[1]);
                console.log("new depth " + depth);
            }
        }
    }

    /* only ran once */
    function loadCookies() {
        const allcookies = document.cookie.replace(/ /g, '').split(';');
        let foundConstrain = false;
        let foundTheme = false;

        for (let i = 0; i < allcookies.length; i++) {
            const kv = allcookies[i].split('=');
            if (kv[0] === 'constrain') {
                constrain = (kv[1] === 'true');
                foundConstrain = true;
                if (constrain === true) {
                    updateConstrain();
                }
            } else if (kv[0] === 'theme') {
                setTheme(kv[1]);
                foundTheme = true;
            }
        }

        if (!foundConstrain) {
            document.cookie = 'constrain=false';
            constrain = false;
        }

        if (!foundTheme) {
            document.cookie = 'theme=default';
            setTheme('default');
        }

        const date = new Date();
        // Expire 7 days from now
        date.setTime(date.getTime() + 604800000);
        document.cookie = 'expires='+date.toGMTString();
    }

    function setTheme(theme) {
        themeableElements.forEach(function(querySelector) {
            const element = document.querySelector(querySelector);
            element.classList.remove('solarized');
            element.classList.remove('eighties');
            element.classList.add(theme);
        });

        document.querySelector('input[name=theme][value=' + theme + ']').checked = true;
    }

    function setCookie(key, value) {
        document.cookie = key + '=' + value.toString();
    }

    /* ══════════════════════════════════ ೋღ SETUP ღೋ ════════════════════════════════ */
    function setup() {
        loadElements();
        addEventListeners();
        setupAutocomplete();

        depth = START_DEPTH;
        lat = START_LAT;
        lon = START_LON;

        handleHashParameters();
        handleDimensionChange();
        loadCookies();
        updateImg();
    }

    function loadElements() {
        mapBody = document.getElementById('mapbody');
        routeStatus = document.getElementById('status-route');
        loadingStatus = document.getElementById('status-loading');
        errorStatus = document.getElementById('status-error');
        warningsContainer = document.getElementById('status-warnings');
        directionsText = document.getElementById('directions-text');
        map = document.getElementById('map');
        dest = document.getElementById('dest');
        routingCanvas = document.getElementById('routing-display');
        hide(dest);
    }

    function addEventListeners() {
        map.onload = handleMapLoad;
        document.getElementById('zoomin').onclick = zoomIn;
        document.getElementById('zoomout').onclick = zoomOut;
        document.getElementById('clear').onclick = clearRoute;
        mapBody.onmousedown = startMouseMoveHandler;

        window.addEventListener('wheel', handleScrollZoom);
        window.addEventListener('resize', handleWindowResize);
        window.addEventListener('hashchange', handleHashChange);
        document.onkeydown = handleKeyPress;
        let searchBox = document.getElementById('search');
        searchBox.onmouseenter = function() {
            activate(document.getElementById('actions'));
        };
        searchBox.onmouseleave = function() {
            deactivate(document.getElementById('actions'));
        };

        document.getElementById('info').onclick = function(event) {
            event.target.classList.toggle('active');
            deactivate(document.getElementById('settings-container'));
        };

        document.getElementById('fa-cog').onclick = showSettings;

        document.getElementById('close-settings').onclick = function() {
            deactivate(document.getElementById('settings-container'));
        };

        document.getElementById('fa-map-signs').onclick = function() {
            activate(document.getElementById('directions-container'));
        };

        document.getElementById('fa-map-signs').onclick = function () {
            activate(document.getElementById('directions-container'));
        };

        document.getElementById('close-directions').onclick = function() {
            deactivate(document.getElementById('directions-container'));
        };

        document.querySelector('body').ondblclick = handleDoubleClick;


        document.getElementById('constrain-input').onchange = function (event) {
            constrain = event.target.checked;
            updateConstrain();
            setCookie('constrain', constrain);
            updateImg();
        };

        document.querySelectorAll('input[type=radio][name=theme]').forEach(function (element) {
            element.onchange = handleThemeChange;
        });

        map.ondragstart = preventAction;
        map.ondrag = preventAction;

        dest.onmousedown = preventAction;
        dest.ondragstart = preventAction;
        dest.ondrag = preventAction;
    }

    function preventAction(event) {
        event.preventDefault();
        return false;
    }

    function setupAutocomplete() {
        /* Make search bar do autocomplete things */
        const autocompleteInput = document.getElementById('search');
        autocomplete({
            input: autocompleteInput,
            fetch: updateAutocomplete,
            onSelect: makeDetailedSearchRequest
        });
    }

    function updateAutocomplete(text, update) {
        fetch(SEARCH_SERVER + '?term=' + text)
              .then(checkStatus)
              .then(processSearchResponse)
              .then(update)
              .catch(console.error);
    }

    function processSearchResponse(rawData) {
        let data = JSON.parse(rawData);
        let result = [];
        for (let i = 0; i < data.length; i++) {
            result.push({
                label: data[i],
                value: data[i],
            })
        }
        return result;
    }

    function makeDetailedSearchRequest(item) {
        document.getElementById('search').value = item.label;
        fetch(SEARCH_SERVER + '?term=' + item.label + '&full=true')
            .then(checkStatus)
            .then(processSelectResponse)
            .catch(console.error);
    }

    function processSelectResponse(rawData) {
        let data = JSON.parse(rawData);

        removeMarkers();
        for (let i = 0; i < data.length; i++) {
            const ele = document.createElement('img');
            ele.id = 'marker_' + data[i].id;
            ele.src = 'round_marker.gif';
            ele.classList.add('rmarker');
            document.getElementById('markers').appendChild(ele);
            ele.onmousedown = preventAction;
            ele.ondrag = preventAction;
            markers.push({lat: data[i].lat, lon: data[i].lon, element: ele});
        }
        updateT();
    }

    function handleMapLoad() {
        console.log('Updating map with image: ' + map.width + 'x' + map.height);
        let warnings = [];
        if (map.width > width + 512) {
            warnings.push("got much wider image than expected. window width: " + width + ". got: " + map.width);
        }
        if (map.height > height + 512) {
            warnings.push("got much taller image than expected. window height: " + height + ". got: " + map.height);
        }
        if (warnings.length > 0) {
            let element = document.createElement('div');
            element.classList.add('card-content');
            element.innerHTML = "Warnings:<br />" + warnings.join("<br />");
            warningsContainer.appendChild(element);
            show(warningsContainer);
        } else {
            hide(warningsContainer);
        }
    }


    /* ══════════════════════════════════ ೋღ EVENTS ღೋ ════════════════════════════════ */

    function handleDoubleClick(event) {
        if (route_params.start_lon && route_params.end_lon) { //finished routing, reset routing
            route_params = {};
            clearRoute();
        }
        const offset = mapBody.getBoundingClientRect();
        const viewBounds = get_view_bounds();
        let click_lon = (event.pageX - offset.left) * get_londpp() + viewBounds.ullon;
        let click_lat = (event.pageY - offset.top) * get_latdpp() + viewBounds.ullat;

        if (route_params.start_lon) { // began routing already but not finished
            route_params.end_lon = click_lon;
            route_params.end_lat = click_lat;
            hide(routeStatus);
            updateRoute();
            setPosition(dest, event.pageX - 12.5, event.pageY - 25);
            show(dest);
            updateImg();
        } else {
            route_params.start_lon = click_lon;
            route_params.start_lat = click_lat;
            show(routeStatus);
        }
    }

    function handleThemeChange(event) {
        setCookie('theme', event.target.value);
        setTheme(event.target.value);
    }

    function startMouseMoveHandler(event) {
        if (event.which !== 1) {
            return; // ignore non-left clicks
        }

        let startX = event.pageX;
        let startY = event.pageY;

        mapBody.onmousemove = createMapMoveHandler(startX, startY, lon, lat);
        mapBody.onmouseup = stopMapMoveHandler;
        mapBody.onmouseleave = stopMapMoveHandler;
    }

    function createMapMoveHandler(startX, startY, startLon, startLat) {
        return function (event) {
            const dx = event.pageX - startX;
            const dy = event.pageY - startY;
            lon = startLon - (dx * get_londpp());
            lat = startLat - (dy * get_latdpp());
            updateT();
        }
    }

    function stopMapMoveHandler() {
        mapBody.onmousemove = null;
        mapBody.onmouseup = null;
        mapBody.onmouseleave = null;
        conditionalUpdate();
    }

    function showSettings() {
        document.getElementById('settings-container').classList.add('active');
        if (constrain) {
            document.getElementById('constrain-input').checked = true;
        }
        document.getElementById('info').classList.remove('active');
    }

    function handleScrollZoom(event) {
        if (event.deltaY < 0) {
            zoomIn();
        } else {
            zoomOut();
        }
    }

    function handleWindowResize() {
        handleDimensionChange();
        updateImg();
    }

    function handleHashChange() {
        handleHashParameters();
        updateImg();
    }

    /* Keyboard navigation callbacks */
    function handleKeyPress(e) {
        let delta = base_move_delta;
        switch (e.keyCode) {
            case 37: //left
                lon -= delta * get_londpp();
                conditionalUpdate();
                break;
            case 38: //up
                lat -= delta * get_latdpp();
                conditionalUpdate();
                break;
            case 39: //right
                lon += delta * get_londpp();
                conditionalUpdate();
                break;
            case 40: //down
                lat += delta * get_latdpp();
                conditionalUpdate();
                break;
            case 189: //minus
                zoomOut();
                break;
            case 187: //equals/plus
                zoomIn();
                break;
        }
    }

    function clearRoute() {
        hide(dest);
        path = undefined;
        directionsText.innerHTML = 'No routing directions to display.';
        drawRoute();
    }

    function hide(element) {
        element.classList.add('hidden');
    }

    function show(element) {
        element.classList.remove('hidden');
    }

    function activate(element) {
        element.classList.add('active');
    }

    function deactivate(element) {
        element.classList.remove('active');
    }

    function setPosition(element, x, y) {
        element.style.left = x + 'px';
        element.style.top = y + 'px';
    }

    function createUrlParams(json) {
        return '?' + Object.keys(json).map(function(k) {
            return encodeURIComponent(k) + '=' + encodeURIComponent(json[k])
        }).join('&');
    }

    function checkStatus(response) {
        if (response.status >= 200 && response.status < 300) {
            return response.text();
        } else {
            return Promise.reject(new Error(response.status +
                ": " + response.statusText));
        }
    }
})();
