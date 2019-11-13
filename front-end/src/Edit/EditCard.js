import React from 'react';
import { useParams } from 'react-router-dom'
import './EditCard.css'
import EditPage from './EditPage'
import {Tabs, Tab, Button} from 'react-bootstrap'
import AddCard from '../Add/AddElement'
import EditElement from './EditElement'
//eslint-disable-next-line      
import { baseUrl } from '../api-js/api-interaction'

const initialCard = {
    'frontPage': {
        'elements': []
    },
    'leftPage': {
        'elements': []
    },
    'rightPage': {
        'elements': []
    },
    'backPage': {
        'elements': []
    }
}

function EditCard() {
    const {cardID} = useParams();
    const [card, setCard] = React.useState(initialCard)
    const [showAddElement, setShowAddElement] = React.useState(false)
    const [positionChanges, setPositionChanges] = React.useState({'frontPage': [], 'leftPage': [], 'rightPage': []})
    const [selectedElt, setSelectedElt] = React.useState({})
    const [update, setUpdate] = React.useState(0)

    React.useEffect(() => {

        var xhr = new XMLHttpRequest();
        xhr.open("GET", `${baseUrl}/getCard/${cardID}`, true);
        xhr.send();
    
        // This will process results and update HTML as appropriate. 
        xhr.onloadend = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                let card = JSON.parse(xhr.responseText).card
                setCard(card)
                console.log(card)
                setPositionChanges({
                    'frontPage': card.frontPage.elements,
                    'leftPage': card.leftPage.elements,
                    'rightPage': card.rightPage.elements,
                })
            } else {
                console.log('error')  
            }
        };

        // Call get one card here
        // setCard({
        //     "cardID": cardID,
        //     "event": "Birthday",
        //     "recipient": "Cormac",
        //     "orientation": "Landscape",
        //     "frontPage": {
        //         "pageID": 123,
        //         "isModifiable": true,
        //         "elements": [
        //             {
        //                 uuid: 1,
        //                 eltType: 'Text',
        //                 text:'frontPage',
        //                 xPosition: 100,
        //                 yPosition: 300,
        //                 height: 200,
        //                 width: 200,
        //             }
        //         ]
        //     },
        //     "leftPage": {
        //         "pageID": 10,
        //         "isModifiable": true,
        //         "elements": [
        //             {
        //                 uuid: 2,
        //                 eltType: 'Text',
        //                 text:'leftPage',
        //                 xPosition: 100,
        //                 yPosition: 100,
        //                 height: 200,
        //                 width: 400,
        //             }
        //         ]
        //     },
        //     "rightPage": {
        //         "pageID": 8,
        //         "isModifiable": true,
        //         "elements": [
        //             {
        //                 uuid: 3,
        //                 eltType: 'Text',
        //                 text:'rightPage',
        //                 xPosition: 100,
        //                 yPosition: 100,
        //                 height: 100,
        //                 width: 500,
        //             }
        //         ]
        //     },
        //     "backPage": {
        //         "pageID": 2, 
        //         "isModifiable": false,
        //         "elements": [
        //             {
        //                 uuid: 4,
        //                 eltType: 'Text',
        //                 text:'backPage',
        //                 xPosition: 100,
        //                 yPosition: 100,
        //                 height: 100,
        //                 width: 200,
        //             }
        //         ]
        //     },
        // })

        // setPositionChanges({
        //     'frontPage': [
        //         {
        //             uuid: 1,
        //             x: 0,
        //             y: 0
        //         }
        //     ],
        //     'leftPage': [
        //         {
        //             uuid: 2,
        //             x: 0,
        //             y: 0
        //         }
        //     ],
        //     'rightPage': [
        //         {
        //             uuid: 3,
        //             x: 0,
        //             y: 0
        //         }
        //     ]
        // })
        
    }, [cardID]) // only re-run if cardID changes

    const saveCard = () => {

        // Use the position changes to update the new positions before sending to database

        let c = card;
        console.log(card)

        const updatePositions = (page) => {
            let arr = []
            positionChanges[page].forEach(e => {
                let matching_el = c[page].elements.filter(function(el) { return el.uuid === e.uuid; })[0];
                if ((e.x !== 0 || e.y !== 0) && matching_el.updated !== 2) {
                    matching_el.xPosition = matching_el.xPosition + e.x;
                    matching_el.yPosition = matching_el.yPosition + e.y; 
                    matching_el.updated = 1;
                }
                arr.push(matching_el)
            })
            return arr;
        }
        
        c.frontPage.elements = updatePositions('frontPage');
        c.leftPage.elements = updatePositions('leftPage');
        c.rightPage.elements = updatePositions('rightPage');

    }

    const addElement = (eltInfo, page) => {

        let c = card;
        if (page === 'Front Page') {
            c.frontPage.elements.push(eltInfo)
        } else if (page === 'Left Page') {
            c.leftPage.elements.push(eltInfo)
        } else {
            c.rightPage.elements.push(eltInfo)
        }

        setCard(c)
        setShowAddElement(false)
    }

    const openAddElement = () => {
        setShowAddElement(true)
    }
    const closeAddElement = () => {
        setShowAddElement(false)
    }

    const updateElt = (elt) => {
        let c = card;
        c['frontPage'].elements.forEach(e => {
            if(e.uuid === elt.uuid){ 
                e = elt
            }
        })
        c['leftPage'].elements.forEach(e => {
            if(e.uuid === elt.uuid){ 
                e = elt 
            }
        })
        c['rightPage'].elements.forEach(e => {
            if(e.uuid === elt.uuid){ 
                e = elt 
            }
        })
        setCard(c)
        setUpdate(update + 1)
    } 
    const deleteElt = (eltID) => {
        let c = card;
        c['frontPage'].elements.forEach(e => {
            if(e.uuid === eltID){ 
                e.updated = 2;
            }
        })
        c['leftPage'].elements.forEach(e => {
            if(e.uuid === eltID){ 
                e.updated = 2;
            }
        })
        c['rightPage'].elements.forEach(e => {
            if(e.uuid === eltID){ 
                e.updated = 2;
            }
        })
        setCard(c)
        setUpdate(update + 1)
    }

    const updateEltPos = (elID, newCoords, pageName) => {

        let pc = positionChanges;
        pc[pageName].forEach(el => {
            if(el.uuid === elID) {
                el.x = newCoords.x
                el.y = newCoords.y
            }
        });

        setPositionChanges(pc)

    }

    const handleElementSelect = (elID, pageName) => {
        let elt = {}
        card[pageName].elements.forEach(e => {
            if(e.uuid === elID) {
                elt = e
            }
        })
        setSelectedElt(elt)
    }

    return (
        <div className="card-wrapper">
            <h1>{card.recipient}: {card.eventType}</h1>
            <Tabs defaultActiveKey="front">
                <Tab eventKey="front" title="Front" className='w-100'>
                    <EditPage selectedID={selectedElt.uuid} page={card.frontPage} update={update} landscape={card.orientation} pageName='frontPage' updatePosition={updateEltPos} handleElementSelect={handleElementSelect}/>
                </Tab>
                <Tab eventKey="left" title="Left" className="tab">
                    <EditPage selectedID={selectedElt.uuid} page={card.leftPage} update={update} landscape={card.orientation} pageName='leftPage' updatePosition={updateEltPos} handleElementSelect={handleElementSelect}/>
                </Tab>
                <Tab eventKey="right" title="Right">
                    <EditPage selectedID={selectedElt.uuid} page={card.rightPage} update={update} landscape={card.orientation} pageName='rightPage' updatePosition={updateEltPos} handleElementSelect={handleElementSelect}/>
                </Tab>
                <Tab eventKey="back" title="Back">
                    <EditPage selectedID={selectedElt.uuid} page={card.backPage} update={update} landscape={card.orientation} pageName='backPage' updatePosition={updateEltPos} handleElementSelect={handleElementSelect}/>
                </Tab>
            </Tabs>
            <div className="w-50 d-flex justify-content-between mb-2">
                <Button className="btn-success" onClick={saveCard}>Save Card</Button>
                <EditButton elt={selectedElt} updateElt={updateElt} deleteElt={deleteElt}/>
                <Button className="btn-success" onClick={openAddElement}>Add Element</Button>
            </div>
            <AddCard card={card} show={showAddElement} addElement={addElement} handleClose={closeAddElement}/>
        </div>
    )   
}

function EditButton(props) {
    const [show, setShow] = React.useState(false)

    const showEditModal = () => {
        setShow(true)
    }
    const hideEditModal = () => {
        setShow(false)
    }
    const updateElt = (elt) => {
        hideEditModal()
        props.updateElt(elt)
    }
    const deleteElt = (eltID) => {
        hideEditModal()
        props.deleteElt(eltID)
    }

    if(Object.entries(props.elt).length !== 0) {
        return (
            <div>
                <Button className='btn-warning' onClick={showEditModal}>Edit Element</Button>
                <EditElement show={show} hide={hideEditModal} elt={props.elt} updateElt={updateElt} deleteElt={deleteElt}/>
            </div>
        )
    } else {
        return (
            <div/>
        )  
    }
}

export default EditCard