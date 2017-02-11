/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.handler;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * base handler for the nest code, all the nest handlers are extended from this one..
 *
 * @author David Bennett - Initial contribution
 */
abstract class BaseNestHandler extends BaseThingHandler {
    private Logger logger = LoggerFactory.getLogger(BaseNestHandler.class);

    BaseNestHandler(Thing thing) {
        super(thing);
    }
}
